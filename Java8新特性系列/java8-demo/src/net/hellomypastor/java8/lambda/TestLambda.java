package net.hellomypastor.java8.lambda;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * java8内置了四大核心的函数式接口
 *
 * Consumer<T> :消费型接口 void accept(T t);
 *
 * Supplier<T> :供给型接口 T get();
 *
 * Function<T,R> :函数型接口 R apply(T t)
 *
 * Predicate<T> :断言型接口 boolean test(T t);
 */
public class TestLambda {
	// Consumer消费者:适用于无返回值，有一个入参
	@Test
	public void testConsumer() {
		Consumer<String> consumer = new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
			}
		};

		consumer.accept("hello");

		Consumer<String> consumer1 = (x) -> {
			System.out.println(x);
		};

		consumer1.accept("hello");

		Consumer<String> consumer2 = System.out::println;

		consumer2.accept("hello");
	}

	// Supplier生产者：适用于无入参，有返回值
	@Test
	public void testSupplier() {
		Supplier<Double> supplier = new Supplier<Double>() {
			@Override
			public Double get() {
				return Math.random();
			}
		};

		System.out.println(supplier.get());

		Supplier<Double> supplier1 = () -> Math.random();

		System.out.println(supplier1.get());

		Supplier<Double> supplier2 = Math::random;

		System.out.println(supplier2.get());
	}

	// 适用于一个入参有返回值
	@Test
	public void testFunction() {
		Function<String, Integer> fun = new Function<String, Integer>() {
			@Override
			public Integer apply(String t) {
				return Integer.parseInt(t);
			}
		};

		System.out.println(fun.apply("555"));

		Function<String, Integer> fun1 = (x) -> Integer.parseInt(x);

		System.out.println(fun1.apply("666"));

		Function<String, Integer> fun2 = Integer::parseInt;

		System.out.println(fun2.apply("777"));
	}

	// 适用于断言
	@Test
	public void testPredicate() {
		Predicate<String> predicate = new Predicate<String>() {
			@Override
			public boolean test(String t) {
				return t.isEmpty();
			}
		};

		System.out.println(predicate.test("123"));
		System.out.println(predicate.test(""));

		Predicate<String> predicate1 = (x) -> x.isEmpty();
		System.out.println(predicate1.test("123"));
		System.out.println(predicate1.test(""));

		Predicate<String> predicate2 = String::isEmpty;
		System.out.println(predicate2.test("123"));
		System.out.println(predicate2.test(""));
	}

	@Test
	public void testBiPredicate() {
		BiPredicate<String, String> biPredicate = new BiPredicate<String, String>() {
			@Override
			public boolean test(String t, String u) {
				return t.equals(u);
			}
		};
		System.out.println(biPredicate.test("1", "2"));
		System.out.println(biPredicate.test("1", "1"));

		BiPredicate<String, String> biPredicate1 = (x, y) -> x.equals(y);
		System.out.println(biPredicate1.test("1", "2"));
		System.out.println(biPredicate1.test("1", "1"));

		BiPredicate<String, String> biPredicate2 = String::equals;
		System.out.println(biPredicate2.test("1", "2"));
		System.out.println(biPredicate2.test("1", "1"));
	}

	@Test
	public void test() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				System.out.println("sss");
			}
		};

		Runnable runnable2 = () -> System.out.println("ccc");
	}
}
