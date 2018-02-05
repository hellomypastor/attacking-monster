package net.hellomypastor.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * （1）java8中Collection接口增加了获取流的方法 default S tream<E> stream()：返回一个串行流 default
 * Stream<E> parallelStream():返回一个并行流 （2）java8中的Arrays提供一些静态方法stream()获取数组流
 * （3）由值创建流：可以用静态方法Stream.of()，通过显式值创建一个stream，它接收任意数量的参数
 * （4）Stream.iterate()和Stream.generate()创建无限流
 */
public class TestCreateStream {
	// java8中Collection接口增加了获取流的方法
	@Test
	public void test1() {
		List<String> strs = Arrays.asList("bb", "aa", "ff", "ee", "cc");

		// 串行流
		Stream<String> stream = strs.stream();
		stream.forEach((str) -> System.out.println(Thread.currentThread().getName() + ":" + str));

		System.out.println("----------");

		// 并行流
		Stream<String> parallelStream = strs.parallelStream();
		parallelStream.forEach((str) -> System.out.println(Thread.currentThread().getName() + ":" + str));
	}

	// java8中的Arrays提供一些静态方法stream()获取数组流
	@Test
	public void test2() {
		Integer[] nums = { 5, 2, 8, 4, 3 };
		Stream<Integer> stream = Arrays.stream(nums);
		stream.forEach(System.out::println);
	}

	// 由值创建流：可以用静态方法Stream.of()，通过显式值创建一个stream，它接收任意数量的参数
	@Test
	public void test3() {
		Stream<String> charsStream = Stream.of("g", "b", "r", "z", "a");
		charsStream.forEach(System.out::println);
	}

	// Stream.iterate()创建无限流
	@Test
	public void test4() {
		// 迭代:下一个元素在前一个元素的基础上生成
		Stream<Integer> stream = Stream.iterate(0, (x) -> x + 2);
		stream.forEach(System.out::println);
		stream.limit(100).forEach(System.out::println);
	}

	// Stream.generate()创建无限流
	@Test
	public void test5() {
		// 生成：下一个元素跟其他元素都没有关系
		Stream<Double> generateStream = Stream.generate(Math::random);
		generateStream.forEach(System.out::println);
		generateStream.limit(10).forEach(System.out::println);
	}
}
