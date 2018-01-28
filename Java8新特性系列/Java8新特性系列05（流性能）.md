![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-4906a3afbf34e9fb.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期介绍了Java8中`Stream`的新特性，本期我们将测试下`stream`与`parallelStream`的性能以及应用的场景。

##先上代码

```java
public class StreamTest {

	private static final int MAX_INT = 1_000_000;

	public static void stream() {
		List<String> list = new ArrayList<>();
		IntStream.range(0, MAX_INT).forEach(value -> {
			UUID uuid = UUID.randomUUID();
			list.add(uuid.toString());
		});

		long startTime = System.nanoTime();

		list.stream().sorted().collect(Collectors.toList());

		long endTime = System.nanoTime();
		long durationTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
		System.out.println("stream execute time : " + durationTime);
	}

	public static void parallelStream() {
		List<String> list = new ArrayList<>();
		IntStream.range(0, MAX_INT).forEach(value -> {
			UUID uuid = UUID.randomUUID();
			list.add(uuid.toString());
		});

		long startTime = System.nanoTime();

		list.parallelStream().sorted().collect(Collectors.toList());

		long endTime = System.nanoTime();
		long durationTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
		System.out.println("parallelStream execute time : " + durationTime);
	}

	public static void main(String[] args) {
		stream();
		parallelStream();
	}
}
```

>MAX_INT = 1_000_000; //Jav8中数字可以用_间隔，类似1,000,000

Max_INT为**1**时，结果为：
stream execute time : **6**
parallelStream execute time : **8**

Max_INT为**100**时，结果为：
stream execute time : **7**
parallelStream execute time : **7**

Max_INT为**1_000**时，结果为：
stream execute time : **15**
parallelStream execute time : **22**

Max_INT为**10_000**时，结果为：
stream execute time : **28**
parallelStream execute time : **21**

Max_INT为**100_000**时，结果为：
stream execute time : **98**
parallelStream execute time : **62**

Max_INT为**1_000_000**时，结果为：
stream execute time : **742**
parallelStream execute time : **429**

Max_INT为**5_000_000**时，结果为：
stream execute time : **4299**
parallelStream execute time : **2191**

Max_INT为**10_000_000**时，结果为：
stream execute time : **9849**
parallelStream execute time : **6923**

##分析
>并行适用的场景？

+ 有大量的元素要处理
+ 性能问题是首要考虑的
+ 没有在一个多线程的环境中

>所以如Java Web应用，底层都是Servlet，我们知道，Servlet是多线程的，所以在web应用中并行流并不适用，而对于数据的处理、算法的验证等单线程环境是适用的。

##原理

并行流底层其实是`ForkJoinPool` ，用的是`分治法`，即`Fork/Join`方法

当执行新的任务时它可以将其拆分分成更小的任务执行，并将小任务加到线 程队列中，然后再从一个随机线程的队列中偷一个并把它放在自己的队列中。

相对于一般的线程池实现，fork/join框架的优势体现在对其中包含的任务的处理方式上，在一般的线程池中，如果一个线程正在执行的任务由于某些原因无法继续运行,那么该线程会处于等待状态。而在fork/join框架实现中，如果某个子问题由于等待另外一个子问题的完成而无法继续运行.那么处理该子问题的线程会主动寻找其他尚未运行的子问题来执行。这种方式减少了线程的等待时间，提高了性能。

```java
public static void main(String[] args) {
	ForkJoinPool pool = ForkJoinPool.commonPool();
	System.out.println(pool.getParallelism());
}
```
结果：
3

我们可以通过参数来修改：

![WX20171230-125634@2x.png](http://upload-images.jianshu.io/upload_images/2855474-954220c822b534f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

结果：
10

##总结
>如何高效使用并行流？

+ 如果用循环还是顺序流或者是并行流，像我们上面那样测试一下；
+ 注意装箱，尽量使用`IntStream`, `LongStream`，和`DoubleStream`来避免装箱拆箱;
+ 有些操作在并行流上性能很差，比如`limit`，`findFirst`等依赖顺序的操作。`unordered`方法可以把有序流转为无序流，使用`findAny`等好很多，在无序流上用`limit`也好很多;
+ 计算流水线操作总成本，处理单个元素用时越多，并行就越划算；
+ 对于较小的数据量，用并行不一定是好事儿；
+ 区分单线程和多线程，多线程下并行不一定是好事儿；
+ 数据结果是否易于分解，比如`ArrayList`比`LinkedList`易于分解，`range`创建的原始流也易于分解；
+ 终端操作中的合并大家是否很大，大了也不划算。