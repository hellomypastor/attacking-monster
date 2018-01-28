![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-e1dde9c2f9d0cded.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分析了Java8中的引用，本期我们将分析Java8中的另一个重要的新特性：流Stream。
>本文**`图片`**转载自[并发编程网](http://ifeve.com/stream/)

##Stream是什么？
在Java8源代码中，是这么定义Stream的：
>A sequence of elements supporting sequential and parallel aggregate operations.

简单翻译就是流是支持顺序和并行的汇聚操作的一组元素。

从这个定义上来说，`Stream`可以说是一个高级版本的`Iterator`，Iterator只能一个一个遍历元素从而对元素进行操作，但是Stream可以执行非常复杂的查找、过滤和映射数据等操作，并且中间操作可以一直迭代。

**Collections是存储元素，Stream是计算。**

Stream可以理解为一个管道（`Pipeline`），数据从管道的一边进入，经过中间各种处理，然后从管道的另一边出来新的数据。

几个注意点：

+ 1. Stream自己不会存储元素。
+ 2. Stream不会改变原对象。相反，他们会返回一个持有结果的新Stream。
+ 3. Stream操作是延迟执行。这意味着他们会等到需要结果的时候才执行。

##Stream的pipeline
+ 创建Stream
+ 中间操作：一个中间操作链，对数据源数据进行处理，但是是延迟执行的
+ 终止操作：执行中间操作链，并产生结果，正如上面注意点3

##创建Stream
####1、java.util.Collection内置了获取流的方法，分别为串行流与并行流

```java
default Stream<E> stream() {
    return StreamSupport.stream(spliterator(), false);
}

default Stream<E> parallelStream() {
    return StreamSupport.stream(spliterator(), true);
}
```

####2、java.util.Arrays内置了获取流的方法

```java
public static <T> Stream<T> stream(T[] array) {
    return stream(array, 0, array.length);
}
```

####3、java.util.stream.Stream内置了创建流的方法，分别为通过对象创建流和通过函数创建流

```java
public static<T> Stream<T> of(T t) {
    return StreamSupport.stream(new Streams.StreamBuilderImpl<>(t), false);
}

public static<T> Stream<T> of(T... values) {
    return Arrays.stream(values);
}

public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f) {
    Objects.requireNonNull(f);
    final Iterator<T> iterator = new Iterator<T>() {
        @SuppressWarnings("unchecked")
        T t = (T) Streams.NONE;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            return t = (t == Streams.NONE) ? seed : f.apply(t);
        }
    };
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator,
                Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
}

public static<T> Stream<T> generate(Supplier<T> s) {
    Objects.requireNonNull(s);
    return StreamSupport.stream(
                new StreamSpliterators.InfiniteSupplyingSpliterator.OfRef<>(Long.MAX_VALUE, s), false);
}
```

##中间操作（java.util.stream.Stream）
####1、截断与切片

+ filter：过滤

```java
Stream<T> filter(Predicate<? super T> predicate);
```
![filter](http://upload-images.jianshu.io/upload_images/2855474-7c65ff0f163e0b9d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ distinct：去除重复元素（通过equals和hashCode）

```java
Stream<T> distinct();
```
![distinct](http://upload-images.jianshu.io/upload_images/2855474-27a903e1c6a4be9a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ limit：限制数量

```java
Stream<T> limit(long maxSize);
```
![limit](http://upload-images.jianshu.io/upload_images/2855474-deae5deec05bc0dd.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ skip：跳过

```java
Stream<T> skip(long n);
```
![skip](http://upload-images.jianshu.io/upload_images/2855474-aa7a3aec635fb1f4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

是不是有点类似SQL语句呢？

####2、映射

+ map

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```
![map](http://upload-images.jianshu.io/upload_images/2855474-fcd527b018c20954.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ mapToInt
+ mapToLong
+ mapToDouble

+ flatMap

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
```
![flatMap](http://upload-images.jianshu.io/upload_images/2855474-2127fe9b745288b5.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

+ flatMapToInt
+ flatMapToLong
+ flatMapToDouble

####3、排序

+ sorted

```java
Stream<T> sorted();
Stream<T> sorted(Comparator<? super T> comparator);
```

####4、包装

+ peek

```java
Stream<T> peek(Consumer<? super T> action);
```
![peek](http://upload-images.jianshu.io/upload_images/2855474-8efc22bb5cb10043.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##终止操作
####查找与匹配

+ allMatch：检查是否匹配所有元素

```java
boolean allMatch(Predicate<? super T> predicate);
```

+ anyMatch：检查是否至少匹配一个元素

```java
boolean anyMatch(Predicate<? super T> predicate);
```

+ noneMatch：检查是否没有匹配所有元素

```java
boolean noneMatch(Predicate<? super T> predicate);
```

+ findFirst：返回第一个元素

```java
Optional<T> findFirst();
```

+ findAny：返回当前流中的任意元素

```java
Optional<T> findAny();
```

+ count：返回流中元素总数

```java
long count();
```

+ max：返回流中最大值

```java
Optional<T> max(Comparator<? super T> comparator);
```

+ min：返回流中最小值

```java
Optional<T> min(Comparator<? super T> comparator);
```

+ forEach：内部迭代

```java
void forEach(Consumer<? super T> action);
```

####规约

+ reduce

```java
T reduce(T identity, BinaryOperator<T> accumulator);

Optional<T> reduce(BinaryOperator<T> accumulator);

<U> U reduce(U identity,
                 BiFunction<U, ? super T, U> accumulator,
                 BinaryOperator<U> combiner);
```

####收集

+ collect

```java
<R, A> R collect(Collector<? super T, A, R> collector);

<R> R collect(Supplier<R> supplier,
                  BiConsumer<R, ? super T> accumulator,
                  BiConsumer<R, R> combiner);
```

+ Collectors静态方法

```java
List<T> toList()
Set<T> toSet()
Collection<T> toCollection
Long counting
Integer summingInt
Double averagingInt
IntSummaryStatistics summarizingInt
String joining
Optional<T> maxBy
Optional<T> minBy
...
```

>Stream是不是很方便呢？
>下期我们将测试下Stream中串行流与并行流的性能