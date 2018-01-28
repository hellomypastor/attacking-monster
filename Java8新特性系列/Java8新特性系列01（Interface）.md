![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-77a1edff91661acb.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>在Java8版本以前，Interface接口中所有的方法都是`抽象方法`和`常量`，那么在Java8中，Interface有什么新特性呢？

##静态成员
>在Java8以前，我们要定义一些常量，一般会写一个类，类中都是`final static`的一些变量，如下：

```java
public class Constants {
	public static final int MAX_SERVICE_TIME = 100;
}

public class Hello {
	public static void main(String[] args) {
		System.out.println(Constants.MAX_SERVICE_TIME);
	}
}
```

>在Java8中Interface支持`静态成员`，成员默认是`public final static`的，可以在类外直接调用。

```java
public interface MyInterface {
	int MAX_SERVICE_TIME = 100;
}

public class Hello {
	public static void main(String[] args) {
		System.out.println(MyInterface.MAX_SERVICE_TIME);
	}
}
```

##default函数
>在Java8以前，Interface中的函数是不能实现的，如下：

```java
public interface MyInterface {
	int MAX_SERVICE_TIME = 100;

	void test();
}
```

>在Java8中，Interface中支持函数有实现，只要在函数前加上`default`关键字即可，如下：

```java
public interface MyInterface {
	int MAX_SERVICE_TIME = 100;

	void test();
	
	default void doSomething() {
		System.out.println("do something");
	}
}
```

>`default`函数，实现类可以不实现这个方法，如果不想子类去实现的一些方法，可以写成`default`函数。
>
>在Java8之前，如果我们想实现这样的功能，也是有办法的，那就是先定义`Interface`，然后定义`Abstract Class`实现`Interface`，然后再定义`Class`继承`Abstract Class`，这样`Class`就不用实现`Interface`中的全部方法。

##static函数
>在Java8中允许Interface定义`static`方法，这允许API设计者在接口中定义像getInstance一样的静态工具方法，这样就能够使得API简洁而精练，如下：

```java
public interface MyInterface {
	default void doSomething() {
		System.out.println("do something");
	}
}

public class MyClass implements MyInterface {

}

public interface MyClassFactory {
	public static MyClass getInstance() {
		return new MyClass();
	}
}
```

##@FunctionalInterface注解
+ 什么是`函数式接口`？

>函数式接口其实本质上还是一个接口，但是它是一种特殊的接口：SAM类型的接口（Single Abstract Method）。定义了这种类型的接口，使得以其为参数的方法，可以在调用时，使用一个`Lambda`表达式作为参数。

```java
@FunctionalInterface
public interface MyInterface {
	void test();
}
```

>`@FunctionalInterface`注解能帮我们检测Interface是否是函数式接口，但是这个注解**是非必须的，不加也不会报错**。

```java
@FunctionalInterface
public interface MyInterface {//报错
	void test();

	void doSomething();
}
```

>Interface中的`非default／static方法都是abstract的`，所以上面的接口不是函数式接口，加上@FunctionalInterface的话，就会提示我们这不是一个函数式接口。

+ 函数式接口的作用？

>函数式接口，可以在调用时，使用一个lambda表达式作为参数。
>
>###所以，Java8下一个特性即是Lambda表达式，请看下回详解。


