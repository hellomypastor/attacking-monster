![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-8eb6d2abe426e9f0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分析了Java8中的Lambda表达式，本期我们将分析Java8中的引用。

##一、方法引用
###定义
>若Lambda体中的功能，已经有方法提供实现，可以使用方法引用，可以将方法引用理解为Lambda 表达式的另外一种表现形式。

###格式
方法引用的具体形式为：

####1. 对象的引用 :: 实例方法名

```java
public class People implements Serializable {
	private static final long serialVersionUID = -2052988928272007869L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static void main(String[] args) {
		People p = new People();
		// Supplier<String> supplier = () -> p.getId();
		Supplier<String> supplier = p::getId;
		// Consumer<String> consumer = id -> p.setId(id);
		Consumer<String> consumer = p::setId;
		...
	}
}
```
    
####2. 类名 :: 静态方法名
```java
public static void main(String[] args) {
	// Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
	Comparator<Integer> comparator = Integer::compare;
	...
}
```

>当需要引用方法的第一个参数是调用对象，并且第二个参数是需要引用方法的第二个参数 (或无参数) 时：ClassName::methodName

####3. 类名 :: 实例方法名
```java
public static void main(String[] args) {
	// Predicate<String> predicate = x -> x.isEmpty();
	Predicate<String> predicate = String::isEmpty;
	...
}
```
###总结：
+ 方法引用所引用的方法的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致(就是函数签名和返回值一致)
+ 若Lambda的参数列表的第一个参数，是实例方法的调用者，第二个参数(或无参)是实例方法的参数时，格式：ClassName::MethodName

##二、构造器引用
###定义
>构造器的参数列表，需要与函数式接口中参数列表保持一致 (就是函数签名一致)

###格式
类名 :: new

```java
public class People implements Serializable {
	private static final long serialVersionUID = -2052988928272007869L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static void main(String[] args) {
		// Supplier<People> supplier = () -> new People();
		Supplier<People> supplier = People::new;
		...
	}
}
```

##三、数组引用
###格式
类型[] :: new

```java
public static void main(String[] args) {
	Function<Integer, People[]> function = People[]::new;
	People[] peoples = function.apply(10);
}
```

>####**下期我们将分析Java8中的流：Stream，敬请期待。**