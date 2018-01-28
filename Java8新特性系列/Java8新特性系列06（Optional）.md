![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-742ea3b642f010e5.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们介绍了Java中的流性能，并行流的应用场景，本期我们介绍Java8种的另一个新特性：`Optional`。

##背景
只要是Java程序员，都应该遇到过空指针异常：`NullPointerException`，简称`NPE`。
在Java8之前，我们都要判断下对象是否为null，或者用`Google`提供的**`Guava`**的Optional
在Java8中，提供了`Optional`

##使用

+ init
    + empty 构造一个空对象
    + of 不能传null
    + ofNullable 可以为null

```java
public static<T> Optional<T> empty() {
    @SuppressWarnings("unchecked")
    Optional<T> t = (Optional<T>) EMPTY;
    return t;
}

public static <T> Optional<T> of(T value) {
    return new Optional<>(value);
}
    
public static <T> Optional<T> ofNullable(T value) {
    return value == null ? empty() : of(value);
}
```

+ get
    + 如果为null，会抛异常，用isPresent来判断

```java
public T get() {
    if (value == null) {
        throw new NoSuchElementException("No value present");
    }
    return value;
}
```

+ isPresent
    + 判断元素是否为null

```java
public boolean isPresent() {
    return value != null;
}
```

+ ifPresent
    + 判断不为null时执行操作，如optional.ifPresent(System.out::println)

```java
public void ifPresent(Consumer<? super T> consumer) {
    if (value != null)
        consumer.accept(value);
}
```

+ filter

```java
public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
    if (!isPresent())
        return this;
    else
        return predicate.test(value) ? this : empty();
}
```

+ map

```java
public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
    if (!isPresent())
        return empty();
    else {
        return Optional.ofNullable(mapper.apply(value));
    }
}
```

+ flatMap

```java
public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        Objects.requireNonNull(mapper);
    if (!isPresent())
        return empty();
    else {
        return Objects.requireNonNull(mapper.apply(value));
    }
}
```

+ orElse
    + 如果为null返回某个默认值，否则返回具体值

```java
public T orElse(T other) {
    return value != null ? value : other;
}
```

+ orElseGet

```java
public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
}
```

+ orElseThrow

```java
public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (value != null) {
        return value;
    } else {
        throw exceptionSupplier.get();
    }
}
```

##总结
>Optional有什么用？

+ 简化代码
    + 类似于Lambda
+ 能一定程度避免空指针
    + 比如：optional.ifPresent(...)等
+ 增加可读性
    + 比如：optional.ifPresent(...).orElse(...);是不是比if／else分支更可读呢？