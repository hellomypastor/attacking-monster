package net.hellomypastor.java8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import net.hellomypastor.java8.entity.Person;

public class TestStream {
	/**
	 * 需求一，输出集合中所有元素 需求二，将字符串集合转化为字符串，以“，”分割
	 */
	@Test
	public void test() {
		// 以前的做法:输出集合中所有元素
		List<String> strs = Arrays.asList("bb", "aa", "ff", "ee", "cc");
		for (String string : strs) {
			System.out.println(string);
		}

		System.out.println("-----------------------------------");

		strs.stream().forEach(System.out::println);

		System.out.println("-----------------------------------");

		// 以前的做法：将字符串集合转化为字符串，以“，”分割
		String formatString = "";
		for (String string : strs) {
			formatString += string + ",";
		}
		String newString = formatString.substring(0, formatString.length() - 1);
		System.out.println(newString);

		System.out.println("-----------------------------------");

		String newString2 = strs.stream().collect(Collectors.joining(","));
		System.out.println(newString2);

		System.out.println("-----------------------------------");

	}

	/**
	 * 需求三，过滤出城市为南京的人员 需求四，将persons按城市分组
	 */
	@Test
	public void test1() {
		List<Person> persons = Arrays.asList(new Person("Tom", 24, "南京"), new Person("Jim", 25, "北京"),
				new Person("Jack", 27, "西安"), new Person("Lucy", 28, "南京"));

		// 以前的做法:过滤出城市为南京的人员
		List<Person> newPersons = new ArrayList<>();
		for (Person person : persons) {
			if (StringUtils.equals("南京", person.getCity())) {
				newPersons.add(person);
			}
		}
		System.out.println(newPersons.size());
		System.out.println("-----------------------------------");

		List<Person> newPersons2 = persons.stream().filter((person) -> StringUtils.equals("南京", person.getCity()))
				.collect(Collectors.toList());
		System.out.println(newPersons2.size());

		System.out.println("-----------------------------------");

		// 以前的做法：将persons按城市分组
		Map<String, List<Person>> groupPersonMap = new HashMap<>();
		Set<String> citySet = new HashSet<>();
		for (Person person : persons) {
			citySet.add(person.getCity());
		}

		for (String city : citySet) {
			List<Person> persons2 = new ArrayList<>();
			for (Person person : persons) {
				if (person.getCity().equals(city)) {
					persons2.add(person);
				}
			}
			groupPersonMap.put(city, persons2);
		}

		System.out.println(groupPersonMap);

		Map<String, List<Person>> collect = persons.stream().collect(Collectors.groupingBy(Person::getCity));

		System.out.println(collect);
	}

}
