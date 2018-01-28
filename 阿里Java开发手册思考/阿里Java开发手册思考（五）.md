![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-fd3b914c4c4e144b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分享了Java中日志的处理（下）：Java中日志实际使用中的相关注意点
>
>本期我们将分享Java中异常的处理

##异常定义
在《java编程思想》中这样定义异常：阻止当前方法或作用域继续执行的问题。
##异常分类
首先我们看下Java中异常的继承关系：

可以看出，`Throwable`有两个子类：`Error`和`Exception`

+ `Error`
    + VirtualMachineError，典型的有`StackOverFlow`和`OutOfMemory`
    + AWTError
+ `Exception`
    + IOException
    + ...
    + RuntimeException

Exception分为CheckedException和UncheckedException，那么CheckedException和UncheckedException区别是什么呢？

+ `UncheckedException`：派生于Error或者RuntimeException的异常
+ `CheckedException`：所有其他的异常

##异常处理机制
异常处理机制分为：抛出异常和捕捉异常

抛出异常：方法上使用`throws`，方法内使用`throw`
捕捉异常：使用`try-catch`或者`try-catch-finally`

原则，正如手册上所说：

+ 不要直接忽略异常
+ 不要用try-catch包住太多语句
+ 不要用异常处理来处理程序的正常控制流
+ 不要随便将异常迎函数栈向上传递，能处理尽量处理

何时向上传播？

+ 当你认为本异常应该由上层处理时，才向上传播

##注意点
+ `finally`语句块一定会执行吗？

>**不一定会**，以下两种情况finally语句块不会执行
>1. 未执行到try语句块
>2. try语句块中有System.exit(0);  

+ `finally`语句块的执行顺序

首先看没有控制语句的情况：

```java
public static void main(String[] args) {
	try {
		System.out.println("try block");
	} finally {
		System.out.println("finally block");
	}
}
```
输出没有疑问：
try block
finally block

>1、如果`try`中有控制语句（`return`、`break`、`continue`），那`finally`语句块是在控制转义语句之前执行还是之后执行？

```java
private static String test1() {
	System.out.println("test1()");
	return "return";
}

private static String test() {
	try {
		System.out.println("try block");
		return test1();
	} finally {
		System.out.println("finally block");
	}
}

public static void main(String[] args) {
	System.out.println(test());
}
```
输出：
try block
test1()
finally block
return

所以说，如果`try`中有控制语句（`return`、`break`、`continue`），那`finally`语句块是在控制转义语句**之前**执行

>2、如果`catch`语句中有控制语句（`return`、`break`、`continue`），那`finally`语句块是在控制转义语句之前执行还是之后执行？

```java
private static String test1() {
	System.out.println("test1()");
	return "return";
}

private static String test() {
	try {
		System.out.println("try block");
		System.out.println(1 / 0);
		return test1();
	} catch (Exception e) {
		System.out.println("catch block");
		return test1();
	} finally {
		System.out.println("finally block");
	}
}

public static void main(String[] args) {
	System.out.println(test());
}
```

输出：
try block
catch block
test1()
finally block
return

所以说，如果`catch`语句中有控制语句（`return`、`break`、`continue`），那`finally`语句块是在控制转义语句**之前**执行

+ `finally`里的变量

```java
public static int test() {
	int i = 0;
	try {
		return i;
	} finally {
		i++;
	}
}

public static void main(String[] args) {
	System.out.println(test());
}
```

输出：
0

咦？很奇怪，为什么是0，而不是1呢？

通过反编译生成的class，我们就能知道原因了

```java
int i = 0;
try {
	return i;
} finally {
	int iTemp = i++;
}
```

原来，i++后只是赋值给了一个新的`局部变量`，i本身并没有变，这一点和函数的形参一样，如果传的是`引用类型`的，那么值会变，如果传的不是引用类型，那么值是不会改变的，改变的也只是局部变量。