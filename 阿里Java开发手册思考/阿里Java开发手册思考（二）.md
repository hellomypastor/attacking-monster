![题图：by pixel2013 From pixabay](http://upload-images.jianshu.io/upload_images/2855474-790d7e670f07899c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>上期我们分享了关于Java中equals与hashCode的理解
>
>本期我们将分享Java中if/else复杂逻辑的处理

>在github上曾看到一些issue，国外的程序员比较忌讳写else，看到了很多这样的评论`else is horrible`，那么对于逻辑很复杂的代码段，如果用太多的if/else的话，那么会导致代码的阅读难度变大，同时会增加代码的`圈复杂度`，理论上，如果一个函数的圈复杂度超过8，那么这个函数就还有可优化的地方，那么如何优化这种多分支的复杂逻辑的函数呢？手册中给出了三种方法：`卫语句`、`策略模式`、`状态模式`，通过阅读`《重构：改善既有代码的设计》`发现，解决这个问题其实有很多种，下面我们就一一道来。


##第一大类：重新组织函数

###1、Extract Method（提炼函数）
>这种方法应该是最常用的方法之一，当函数过长或者分支太多的话，就可以考虑将其中的一段代码提炼成一个独立的函数。

+ 原始代码：

```java
public void today() {
	if (isBusy()) {
		System.out.println("change time.");
	} else if (isFree()) {
		System.out.println("go to travel.");
	} else {
		System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
	}
}
```

+ 改后代码：

```java
public void today() {
	if (isBusy()) {
		changeTime();
	} else if (isFree()) {
		goToTravel();
	} else {
		stayAtHomeToLearn();
	}
}

private void changeTime() {
		System.out.println("change time.");
}

private void goToTravel() {
	System.out.println("go to travel.");
}

private void stayAtHomeToLearn() {
	System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
}
```
用法：

+ 提炼新函数，根据这个函数的意图来命名，以它做什么来命名，而不是以他怎么做来命名
+ 仔细检查提炼出的代码是否引用了作用域限于源函数的变量，包括局部变量和源函数参数
+ 适用场景：函数过长或者需要注释才能让人理解用途的代码

###2、Substitute Algorithm（替换算法）
>把某个算法替换成另一个更清晰的算法，或将函数本体替换为另一个算法。

+ 原始代码：

```java
public String foundPerson(String[] people) {
	for (int i = 0; i < people.length; i++) {
		if (people[i].equals("Don")) {
			return "Don";
		}
		if (people[i].equals("John")) {
			return "John";
		}
		if (people[i].equals("Kent")) {
			return "Kent";
		}
	}
	return "";
}
```

+ 改后代码：

```java
public String foundPerson(String[] people) {
	List<String> candidates = Arrays.asList(new String[] { "Don", "John", "Kent" });
	for (int i = 0; i < people.length; i++) {
		if (candidates.contains(people[i])) {
			return people[i];
		}
	}
	return "";
}
```
用法：

+ 准备好另一个替换用的算法
+ 新算法，要与原本的算法结果相同
+ 适用场景：把某个算法替换为更清晰的算法，或者把函数替换为一个算法

##第二大类：简化条件表达式

###1、Eecompose Conditional（分解条件表达式）
>如果有复杂的条件（if-then-else）语句，从if、then、else三个段落中分别提炼出独立函数。

+ 原始代码：

```java
public void today() {
	if (isBusy() || isNotWeekend()) {
	   System.out.println("change time.");
	return;
	} else {
		System.out.println("go to travel.");
	}
}
```

+ 改后代码：

```java
public void today() {
	if (notFree()) {
		changeTime();
	} else {
		goToTravel();
	}
}

private boolean notFree() {
	return isBusy() || isNotWeekend();
}

private void changeTime() {
	System.out.println("change time.");
}

private void goToTravel() {
	System.out.println("go to travel.");
}
```
用法：

+ 将if段落提炼出来，构成一个独立函数
+ 将then段落和else段落都提炼出来，各自构成一个独立函数
+ 适用场景：复杂的条件语句。如果发现嵌套的条件逻辑，先观察是否可以使用卫语句，如果不行，再开始分解其中的每个条件

###2、Consolidate Conditioinal Expression（合并条件表达式）
>如果有一系列条件测试，都得到相同结果，将这些测试合并为一个条件表达式，并将这个条件表达式提炼成为一个独立函数。

+ 原始代码：

```java
public void today() {
	if (isWeekend()) {
		System.out.println("go to travel.");
	}
	if (isHoliday()) {
		System.out.println("go to travel.");
	}
	if (noWork()) {
		System.out.println("go to travel.");
	}
}
```

+ 改后代码：

```java
public void today() {
	if (isFree()) {
		System.out.println("go to travel.");
	}
}

private boolean isFree() {
	return isWeekend() || isHoliday() || noWork();
}
```
用法：

+ 确定这些条件语句都没有副作用
+ 使用适当的逻辑操作符，将一系列相关条件表达式合并为一个，并对合并后的表达式提炼函数
+ 适用场景：一系列条件测试，都得到相同的结果

###3、Consolidate Duplicate Conditional Clauses（合并重复的条件判断）
>在条件表达式的每个分支上有相同的一段代码，将这段代码搬移到条件表达式之外。

+ 原始代码：

```java
public void today() {
	if (isBusy()) {
		System.out.println("change time.");
		sleep();
	} else if (isFree()) {
		System.out.println("go to travel.");
		sleep();
	} else {
		System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
		sleep();
	}
}
```

+ 改后代码：

```java
public void today() {
	if (isBusy()) {
		System.out.println("change time.");
	} else if (isFree()) {
		System.out.println("go to travel.");
	} else {
		System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
	}
	sleep();
}
```
用法：

+ 鉴别出执行方式不随条件变化而变化的的代码
+ 如果这些共同代码位于条件表达式起始处，就将它移到条件表达式之前，如果在尾端，移到条件表达式之后
+ 适用场景：在条件表达式的每个分支上有相同的一段代码

###4、Remove Control Flag（移除控制标记）
在一系列布尔表达式中，某个变量带有“控制标记（Flag）”的作用，以break语句或者return语句取代控制标记。

###5、Replace Nested Confitional with Guard Clauses（以卫语句取代嵌套条件表达式）
>如果多个分支都属于正常行为，就应该使用if...else...的条件表达式，如果某个条件极其罕见，就应该单独检查该条件，并在该条件为真时立刻从函数中返回，这样的单独检查常常被称为卫语句。

+ 原始代码：

```java
public void today() {
	if (isBusy()) {
		System.out.println("change time.");
	} else if (isFree()) {
		System.out.println("go to travel.");
	} else {
		System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
	}
}
```

+ 改后代码：

```java
public void today() {
	if (isBusy()) {
		System.out.println("change time.");
		return;
	}
	if (isFree()) {
		System.out.println("go to travel.");
		return;
	}
	System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
	return;
}
```
用法：

+ 对于每个检查，放进一个卫语句，卫语句要么就从函数中返回，要么就抛出一个异常
+ 每次将条件检查替换成卫语句后，编译并测试：如果所有的卫语句都导致同样的结果，请使用`合并条件表达式`
+ 适用场景：使用卫语句返回所有特殊情况

###6、Replace Conditional with Polymorphism（以多态取代条件表达式）
>条件表达式，根据对象的类型选择不同的行为，将这个条件表达式的每个分支放进一个子内的覆写函数中，然后将原始函数声明为抽象函数，这一项就是手册中说的策略模式以及状态模式。
正因为有了多态，所以“类型码的switch语句”以及“基于类型名称的if-then-else”语句在面向对象程序中很少出现。

##第三大类：简化函数调用

###1、Separate Query from Modifier（将查询函数和修改函数分离）
>某个函数既返回对象状态值，又修改了状态，建立两个不同的函数，其中一个负责查询，另一个负责修改。

+ 并发的情况：需要保留第三个函数来同时做这两件事

###2、Parameterize Method（令函数携带参数）
>若干函数做了类似的工作，但在函数本体中却包含了不同的值，建立单一函数，以参数表达那些不同的值。

+ 原始代码：

```java
public void tenPercentRaise() {
	salary *= 1.1;
}

public void fivePercentRaise() {
	salary *= 1.05;
}
```

+ 改后代码：

```java
public void raise(double factor) {
	salary *= (1 + factor);
}
```

+ 要点在于：以可将少量数值视为参数为依据，找出带有重复性的代码

###3、Replace Parameter with Explicit Methods（以明确函数取代参数）
>有一个函数，其中完全取决于参数值而采取不同行为，针对该参数的每一个可能值，建立一个独立的函数。

+ 原始代码：

```java
public void setValue(String name, int value) {
	if (name.equals("height")) {
		height = value;
	}

	if (name.equals("width")) {
		width = value;
	}
}
```

+ 改后代码：

```java
public void setHeight(int arg) {
	height = arg;
}

public void setWidth(int arg) {
	width = arg;
}
```

用法：

+ 针对参数的每一种可能值，新建一个明确的函数
+ 修改条件表达式的每个分支，使其调用合适的新函数
+ 适用场景：函数完全取决于参数值而采取不同行为