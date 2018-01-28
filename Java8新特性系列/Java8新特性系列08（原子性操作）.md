![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-d542fe056f5f4e02.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们介绍了Java8中新的时间日期API，本期我们介绍Java8中原子性操作`LongAdder`。

##原子操作
根据百度百科的定义：
>"原子操作(atomic operation)是不需要synchronized"，这是Java多线程编程的老生常谈了。所谓原子操作是指不会被线程调度机制打断的操作；这种操作一旦开始，就一直运行到结束，中间不会有任何 context switch （切换到另一个线程）。

##AtomicLong
在单线程的环境中，使用Long，如果对于多线程的环境，如果使用Long的话，需要加上`synchronized`关键字，从Java5开始，JDK提供了`AtomicLong`类，AtomicLong是一个提供原子操作的Long类，通过线程安全的方式操作加减，AtomicLong提供原子操作来进行Long的使用，因此十分适合高并发情况下的使用。

```java
public class AtomicLongFeature {
	private static final int NUM_INC = 1_000_000;

	private static AtomicLong atomicLong = new AtomicLong(0);

	private static void update() {
		atomicLong.set(0);
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		IntStream.range(0, NUM_INC).forEach(i -> {
			Runnable task = () -> atomicLong.updateAndGet(n -> n + 2);
			executorService.submit(task);
		});
		stop(executorService);
		System.out.println(atomicLong.get());
	}

	private static void stop(ExecutorService executorService) {
		try {
			executorService.shutdown();
			executorService.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (!executorService.isTerminated()) {
				System.out.println("kill tasks");
			}
			executorService.shutdownNow();
		}
	}

	public static void main(String[] args) {
		update();
	}
}
```
输出：
2000000

为什么`AtomicInteger`能支持高并发呢？看下`AtomicLong`的`updateAndGet`方法：

```java
public final int updateAndGet(IntUnaryOperator updateFunction) {
    int prev, next;
    do {
        prev = get();
        next = updateFunction.applyAsInt(prev);
    } while (!compareAndSet(prev, next));
    return next;
}

public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}
```

原因是每次`updateAndGet`时都会调用`compareAndSet`方法。

>AtomicLong是在使用非阻塞算法实现并发控制，在一些高并发程序中非常适合，但并不能每一种场景都适合，不同场景要使用使用不同的数值类。

##LongAdder
AtomicLong的原理是依靠底层的cas来保障原子性的更新数据，在要添加或者减少的时候，会使用死循环不断地cas到特定的值，从而达到更新数据的目的。那么LongAdder又是使用到了什么原理?难道有比cas更加快速的方式？

```java
public class LongAdderFeature {
	private static final int NUM_INC = 1_000_000;

	private static LongAdder longAdder = new LongAdder();

	private static void update() {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		IntStream.range(0, NUM_INC).forEach(i -> {
			Runnable task = () -> longAdder.add(2);
			executorService.submit(task);
		});
		stop(executorService);
		System.out.println(longAdder.sum());
	}

	private static void stop(ExecutorService executorService) {
		try {
			executorService.shutdown();
			executorService.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (!executorService.isTerminated()) {
				System.out.println("kill tasks");
			}
			executorService.shutdownNow();
		}
	}

	public static void main(String[] args) {
		update();
	}
}
```

输出：
2000000

我们来看下LongAdder的add方法：

```java
public void add(long x) {
    Cell[] as; long b, v; int m; Cell a;
    if ((as = cells) != null || !casBase(b = base, b + x)) {
        boolean uncontended = true;
        if (as == null || (m = as.length - 1) < 0 ||
            (a = as[getProbe() & m]) == null ||
            !(uncontended = a.cas(v = a.value, v + x)))
            longAccumulate(x, null, uncontended);
    }
}
```

我们可以看到一个Cell的类，那这个类是用来干什么的呢?

```java
@sun.misc.Contended static final class Cell {
    volatile long value;
    Cell(long x) { value = x; }
    final boolean cas(long cmp, long val) {
        return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long valueOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> ak = Cell.class;
            valueOffset = UNSAFE.objectFieldOffset
                (ak.getDeclaredField("value"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
```

我们可以看到Cell类的内部是一个volatile的变量，然后更改这个变量唯一的方式通过cas。我们可以猜测到LongAdder的高明之处可能在于将之前单个节点的并发分散到各个节点的，这样从而提高在高并发时候的效率。

>LongAdder在AtomicLong的基础上将单点的更新压力分散到各个节点，在低并发的时候通过对base的直接更新可以很好的保障和AtomicLong的性能基本保持一致，而在高并发的时候通过分散提高了性能。

```java
public long sum() {
    Cell[] as = cells; Cell a;
    long sum = base;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```

>当计数的时候，将base和各个cell元素里面的值进行叠加，从而得到计算总数的目的。这里的问题是在计数的同时如果修改cell元素，有可能导致计数的结果不准确，所以缺点是LongAdder在统计的时候如果有并发更新，可能导致统计的数据有误差。