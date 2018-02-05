package net.hellomypastor.java8.optional;

import java.util.Optional;

import org.junit.Test;

import net.hellomypastor.java8.entity.Person;

/**
 * Optional<T>容器类：用于尽量避免空指针异常
        Optional.of(T t):创建一个Optional实例
        Optional.empty():创建一个空的Optional实例
        Optional.ofNullable(T t):若t不为null，创建Optional实例，否则创建空实例
        isPresent()：判断是否包含值
        orElse(T t)：如果调用对象包含值，返回改值，否则返回t
        orElseGet(Supplier s):如果调用对象包含值，返回值，否则返回s获取的值
        map(Function f):如果有值，对其处理并返回处理之后的Optional,否则返回Optional.empty()
 */
public class TestOptional {
	@Test
	public void test1() {
		// Optional.of(T t):创建一个Optional实例
		Optional<Person> personOptional = Optional.of(new Person("tom", 19, "南京"));
		System.out.println(personOptional.get());

		// Optional.empty():创建一个空的Optional实例
		Optional<Person> personOptional1 = Optional.empty();

		// isPresent()：判断是否包含值
		System.out.println(personOptional1.isPresent());

		// Optional.ofNullable(T t):若t不为null，创建Optional实例，否则创建空实例
		Optional<Person> personOptional2 = Optional.ofNullable(new Person("tom", 19, "南京"));
		System.out.println(personOptional2.get());

		// orElse(T t)：如果调用对象包含值，返回改值，否则返回t
		System.out.println(personOptional.orElse(new Person("jack", 26, "北京")));
		System.out.println(personOptional1.orElse(new Person("jack", 26, "北京")));

		// orElseGet(Supplier s):如果调用对象包含值，返回值，否则返回s获取的值
		System.out.println(personOptional1.orElseGet(Person::new));

		// map(Function f):如果有值，对其处理并返回处理之后的Optional,否则返回Optional.empty()
		System.out.println(personOptional.map(Person::getName).get());
	}
}
