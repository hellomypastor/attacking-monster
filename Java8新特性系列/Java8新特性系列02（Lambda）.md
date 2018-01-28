![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-5a1aeff4ceef9009.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分析了Java8中Interface的相关新特性，其中包括函数式接口，可以在调用时，使用一个Lambda表达式作为参数，那么我们就来谈谈Java8中的Lambda表达式吧。

##定义
>Lambda表达式基于函数式接口实现，故可以认为Lambda表达式是对函数式接口的匿名内部类的一种简写形式。

##格式
Lambda表达式的具体形式为：`()->{}`

箭头表达式`->`将Lambda表达式分为了左右两部分，左侧为参数列表，右侧为具体实现，即Lambda体。

具体有以下以及情形：

####1. 无参数无返回值

```java
Runnable runnable = () -> {
	System.out.println("run");
};
```
    
####2. 有一个参数无返回值
```java
public interface MyInterface {
	void test(int x);
}

MyInterface i = (x) -> System.out.println(x);
```

####3. 只要一个参数，小括号可以不写
```java
MyInterface i = x -> System.out.println(x);
```
####4. 有多个参数有返回值，并且Lambda体有多条语句
```java
Comparator<Integer> comparator = (x, y) -> {
    System.out.println("Comparator");
    return Integer.compare(x, y);
};
```
####5. Lambda体中只有一条语句，return和{}可以省略
```java
Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
```
####6. Lambda 表达式的参数列表的数据类型可以省略不写，因为JVM编译器通过上下文推断出，数据类型，即“类型推断”
```java
Comparator<Integer> comparator = (Integer x, Intergery) -> Integer.compare(x, y);
Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
```
###总结：

+ 参数类型可忽略，若写所有参数都要写，若不写，可以类型推断
+ 参数有且仅有一个时，`()`可以省略
+ Lambda体只有一条语句，`return`和`{}`都可忽略

##Java8内置四大核心函数式接口(java.util.function.*包)
+ Consumer<T> 消费型接口

```java
public static void main(String[] args) {
	String str = "str";
	consumer(str, s -> System.out.println(s));
}

public static void consumer(String str, Consumer<String> function) {
	function.accept(str);
}
```

+ Supplier<T> 供给型接口

```java
public static void main(String[] args) {
	supplier(() -> "str");
}

public static String supplier(Supplier<String> function) {
	return function.get();
}
```

+ Function<T, R> 函数型接口

```java
public static void main(String[] args) {
	String str = "str";
	function(str, s -> s);
}
	
public static String function(String str, Function<String, String> function) {
	return function.apply(str);
}
```

+ Predicate<T> 断定型接口

```java
public static void main(String[] args) {
	String str = "str";
	predicate(str, s -> s.isEmpty());
}

public static boolean predicate(String str, Predicate<String> function) {
	return function.test(str);
}
```

>Lambda表达式就到这里了，一开始用起来会不习惯，用多了上手起来就熟练了，而且越用越信手拈来。
>
>####**下期我们将分析用引用方式简写Lambda表达式，敬请期待。**