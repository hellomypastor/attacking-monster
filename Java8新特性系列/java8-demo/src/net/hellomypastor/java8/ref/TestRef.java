package net.hellomypastor.java8.ref;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import net.hellomypastor.java8.entity.Person;

/**
 * 一、 方法引用 若lambda体中的功能，已经有方法提供了实现，可以使用方法引用来替换整个Lambda表达式
 * （可以将方法引用理解为Lambda表达式的另外一种表现形式）
 * 
 * 格式： （1）对象的引用::实例方法名 （2）类名::静态方法名 （3）类名::实例方法名
 * 
 * 使用规则(判断依据)： （1）方法引用所引用的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致
 * （2）若Lambda的参数列表的第一个参数，是实例方法的调用者，第二个参数（或无参）是实例方法的参数时，格式ClassName::MethodName
 * 
 * 二、构造器引用 构造器的参数列表需要与函数式接口中参数列表保持一致 格式： 类名::new
 * 
 * 三、数组引用 格式： 类型[]::new
 */
public class TestRef {

	// 对象的引用::实例方法名
	// 判断依据：方法引用所引用的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致
	@Test
	public void test1() {
		Consumer<String> consumer = new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
			}
		};
		consumer.accept("123");

		PrintStream pt = System.out;

		Consumer<String> consumer1 = (str) -> pt.println(str);
		consumer1.accept("345");

		Consumer<String> consumer2 = pt::println;
		consumer2.accept("56546");
	}

	// 类名::静态方法名
	// 判断依据：方法引用所引用的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致
	@Test
	public void test2() {
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(o1, o2);
			}
		};
		System.out.println(comparator.compare(1, 2));

		Comparator<Integer> comparator1 = (x, y) -> Integer.compare(x, y);
		System.out.println(comparator1.compare(1, 2));

		Comparator<Integer> comparator2 = Integer::compare;
		System.out.println(comparator2.compare(1, 2));
	}

	// 类名::实例方法名
	// 若Lambda的参数列表的第一个参数，是实例方法的调用者，第二个参数（或无参）是实例方法的参数时，格式ClassName::MethodName
	@Test
	public void test3() {
		BiFunction<String, String, Boolean> biFunction = new BiFunction<String, String, Boolean>() {
			@Override
			public Boolean apply(String t, String u) {
				return t.equals(u);
			}
		};

		System.out.println(biFunction.apply("123", "123"));

		BiFunction<String, String, Boolean> biFunction1 = (s1, s2) -> s1.equals(s2);
		System.out.println(biFunction1.apply("111", "222"));

		BiFunction<String, String, Boolean> biFunction2 = String::equals;
		System.out.println(biFunction2.apply("111", "222"));
	}

	// 构造器引用 类名::new
	// 判断依据：构造器的参数列表需要与函数式接口中参数列表保持一致
	@Test
	public void test4() {
		Person person = new Person();
		System.out.println(person);

		Supplier<Person> supplier = new Supplier<Person>() {
			@Override
			public Person get() {
				return new Person();
			}
		};
		System.out.println(supplier.get());

		Supplier<Person> supplier1 = () -> new Person();
		System.out.println(supplier1.get());

		Supplier<Person> supplier2 = Person::new;
		System.out.println(supplier2.get());

		System.out.println("-------------------------");

		Function<String, Person> createPersonFunction = new Function<String, Person>() {
			@Override
			public Person apply(String t) {
				return new Person(t);
			}
		};
		System.out.println(createPersonFunction.apply("jim"));

		Function<String, Person> createPersonFunction1 = (name) -> new Person(name);
		System.out.println(createPersonFunction1.apply("tom"));

		Function<String, Person> createPersonFunction2 = Person::new;
		System.out.println(createPersonFunction2.apply("jack"));
	}

	/**
	 * 数组引用 格式： 类型[]::new
	 */
	@Test
	public void test5() {
		String[] strs = new String[5];

		Function<Integer, String[]> createStrsFunction = new Function<Integer, String[]>() {
			@Override
			public String[] apply(Integer count) {
				return new String[count];
			}
		};
		System.out.println(createStrsFunction.apply(100).length);

		Function<Integer, String[]> createStrsFunction1 = (count) -> new String[count];
		System.out.println(createStrsFunction1.apply(11).length);

		Function<Integer, String[]> createStrsFunction2 = String[]::new;
		System.out.println(createStrsFunction2.apply(17).length);

	}
}
