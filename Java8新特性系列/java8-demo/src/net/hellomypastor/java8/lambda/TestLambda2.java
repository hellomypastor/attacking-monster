package net.hellomypastor.java8.lambda;

import java.util.Comparator;
import java.util.function.BiFunction;

import org.junit.Test;

import net.hellomypastor.java8.interfaces.MyInterface;
import net.hellomypastor.java8.interfaces.ValidName;

/**
 * 1、函数式接口：@FunctionalInterface注解用于检查一个接口是否是函数式接口
 * 
 * lambda表达式是基于函数式接口实现的，可以认为是匿名内部类的一种简写形式
 *
 * 2、lambda表达式
 *
 * 格式：() -> {}
 *
 * 箭头操作符->将lambda表达式拆分成两个部分 左侧：Lambda表达式的参数列表 右侧：Lambda表达式所需实现的功能，即Lambda体
 *
 * 语法格式一：无参无返回值的方法（例：Runnable） () -> {}
 *
 * 语法格式二：有一个参数无返回值的方法 (x) -> {}
 *
 * 语法格式三：有多个参数，且有返回值(例：Comparator) (x,y) -> {}
 *
 *
 * 注意： 1、lambda表达式的参数类型可省略（若写都要写），JVM可根据上下文推断出类型 2、参数列表只有一个参数时“（）”可省略
 * 3、lambda体中只有一条语句时“{}，return，；”均可省略
 */
public class TestLambda2 {
	/**
	 *
	 * test1:无参无返回值的方法.
	 */
	@Test
	public void test1() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				System.out.println("hello1");
			}
		};
		new Thread(runnable).start();

		// lambda体一条语句，不省略{}
		Runnable runnable1 = () -> {
			System.out.println("hello1");
		};
		new Thread(runnable1).start();

		// lambda体一条语句，省略{}和;
		Runnable runnable2 = () -> System.out.println("hello2");
		new Thread(runnable2).start();

		// lambda体多条语句，语句写在大括号里面
		Runnable runnable3 = () -> {
			System.out.println("hello3");
			System.out.println("hello4");
		};
		new Thread(runnable3).start();
	}

	/**
	 * test2:有一个参数无返回值的方法.
	 */
	@Test
	public void test2() {
		// 匿名内部类实现
		PrintName printName = new PrintName() {
			@Override
			public void print(String name) {
				System.out.println(name);
			}
		};
		printName.print("gz");

		// 一条语句，不省略括号
		PrintName printName2 = (x) -> {
			System.out.println(x);
		};
		printName2.print("gz2");

		// 一条语句，省略括号
		PrintName printName3 = x -> System.out.println(x);
		printName3.print("gz3");

		// 多条语句，{}不能省略
		PrintName printName4 = (x) -> {
			System.out.println(x);
			System.out.println(x + "55555");
		};
		printName4.print("gz4");
	}

	/**
	 * test3:一个参数有返回值的方法.
	 */
	@Test
	public void test3() {
		// 匿名内部类实现
		ValidName validName = new ValidName() {
			@Override
			public boolean valid(String name) {
				return name.length() < 10;
			}
		};
		System.out.println(validName.valid("sfsfs"));

		// 不省略实现
		ValidName validName1 = (name) -> {
			return name.length() < 10;
		};
		System.out.println(validName1.valid("sfs"));

		// 省略实现
		ValidName validName2 = name -> name.length() < 10;
		System.out.println(validName2.valid("sfs"));

		// 多条语句
		ValidName validName3 = name -> {

			System.out.println("xxxxx");

			return name.length() < 10;
		};
		System.out.println(validName3.valid("sfs"));
	}

	/**
	 * test4:多个参数有返回值.
	 */
	@Test
	public void test4() {
		// 匿名内部类方式实现
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(o1, o2);
			}
		};
		System.out.println(comparator.compare(1, 10));

		// 不省略大括号
		Comparator<Integer> comparator1 = (x, y) -> {
			return Integer.compare(x, y);
		};
		System.out.println(comparator1.compare(1, 10));

		// 省略大括号
		Comparator<Integer> comparator2 = (x, y) -> Integer.compare(x, y);
		System.out.println(comparator2.compare(1, 10));

		// 多条语句
		Comparator<Integer> comparator3 = (x, y) -> {
			System.out.println("比较大小");
			return Integer.compare(x, y);
		};
		System.out.println(comparator3.compare(1, 10));
	}

	/**
	 * 有多个抽象方法的接口不属于函数式接口，无法用lambda表达式
	 */
	@Test
	public void test5() {
		MyInterface mInterface = new MyInterface() {
			@Override
			public String product(String str) {
				return "---" + str + "---";
			}

			@Override
			public void print(String str) {
				System.out.println(str);
			}
		};

		// MyInterface mInterface1 = (str) -> System.out.println(str);
	}

	/**
	 * 如何在代码中应用lambda表达式？ 应用实例：再也不用写多个接口的实现类了,方法的实现都可以通过参数来传递
	 */
	@Test
	public void test6() {
		caculate(10, 4, (x, y) -> x + y, "两数相加的结果是：");
		caculate(10, 4, (x, y) -> x - y, "两数相减的结果是：");
		caculate(10, 4, (x, y) -> x * y, "两数相乘的结果是：");
		caculate(10, 4, (x, y) -> x / y, "两数相除的结果是：");
		caculate(10, 4, (x, y) -> x % y, "两数取余的结果是：");
		caculate(10, 4, Integer::sum, "两数相加的结果是：");
		caculate(10, 4, Integer::min, "两数中最小的是：");
	}

	private int caculate(int x, int y, Caculator caculator, String msg) {
		int result = caculator.caculate(x, y);
		System.out.println(msg + result);
		return result;
	}

	/**
	 * 应用jdk提供的函数式接口来重写上述实现
	 */
	@Test
	public void test7() {
		caculate2(10, 4, (x, y) -> x + y, "两数相加的结果是：");
		caculate2(10, 4, (x, y) -> x - y, "两数相减的结果是：");
		caculate2(10, 4, (x, y) -> x * y, "两数相乘的结果是：");
		caculate2(10, 4, (x, y) -> x / y, "两数相除的结果是：");
		caculate2(10, 4, (x, y) -> x % y, "两数取余的结果是：");
		caculate2(10, 4, Integer::sum, "两数相加的结果是：");
		caculate2(10, 4, Integer::min, "两数中最小的是：");
	}

	private int caculate2(int x, int y, BiFunction<Integer, Integer, Integer> caculator, String msg) {
		int result = caculator.apply(x, y);
		System.out.println(msg + result);
		return result;
	}
}
