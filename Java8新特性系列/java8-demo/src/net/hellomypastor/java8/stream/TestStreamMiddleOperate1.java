package net.hellomypastor.java8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import net.hellomypastor.java8.entity.Person;

/**
 * 筛选与切片
            filter(Predicate p)：接收断言性Lambda，从流中过滤出一些元素(不满足断言判断的则排除)
            distinct()：筛选，通过元素的hashCode()和equals()方法判断后去重复
            limit(long maxSize)：截断流，使元素不超过给定数量
            skip(long n)：跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流

      映射
            map(Function f): 接收一个函数作为参数，该函数将会被应用到每个元素上，并将其映射成一个新的元素。

      排序
            sorted()：自然排序
            sorted(Comparator comp):定制排序
 */
public class TestStreamMiddleOperate1
{
    private static List<Person> persons = new ArrayList<Person>();

    static
    {
        persons.add(new Person("Tom", 24, "南京"));
        persons.add(new Person("Jim", 19, "北京"));
        persons.add(new Person("Jack", 34, "西安"));
        persons.add(new Person("Lucy", 41, "南京"));
    }

    //filter(Predicate p)：接收断言性Lambda，从流中过滤出一些元素(不满足断言判断的则排除)
    @Test
    public void testFilter()
    {
        //打印南京的人员
        persons.stream().filter(person -> StringUtils.equals(person.getCity(), "南京")).forEach(System.out::println);

        System.out.println("---------------");

        //打印年龄在20-40岁（不包含20、40）之间的人员
        persons.stream().filter(person -> person.getAge() > 20 && person.getAge() < 40).forEach(System.out::println);
    }

    //distinct()：筛选，通过元素的hashCode()和equals()方法判断后去重复
    //limit(long maxSize)：截断流，使元素不超过给定数量
    //skip(long n)：跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流
    @Test
    public void testOthers()
    {
        List<String> strs = Arrays.asList("aaa", "bbb", "aaa", "ccc");

        strs.stream().distinct().forEach(System.out::println);

        System.out.println("------------------------------------");

        strs.stream().limit(2).forEach(System.out::println);

        System.out.println("------------------------------------");

        strs.stream().skip(2).forEach(System.out::println);
    }

    //map(Function f): 接收一个函数作为参数，该函数将会被应用到每个元素上，并将其映射成一个新的元素。
    @Test
    public void testMap()
    {
        persons.stream().map((person) -> person.getName()).forEach(System.out::println);
        persons.stream().map(Person::getName).forEach(System.out::println);
    }

    //sorted()：自然排序
    //sorted(Comparator comp):定制排序
    @Test
    public void testSort()
    {
        List<String> strs = Arrays.asList("aaa", "bbb", "aaa", "ccc");

        strs.stream().sorted().forEach(System.out::println);

        System.out.println("------------------------------------");

        List<String> strs1 = Arrays.asList("aaa", "bbb", "aaa", "ccc");
        strs1.stream().sorted((str1, str2) -> -str1.compareTo(str2)).forEach(System.out::println);

        System.out.println("------------------------------------");

        List<String> strs2 = Arrays.asList("aaa", "bbb", "aaa", "ccc");
        strs2.stream().sorted(String::compareTo).forEach(System.out::println);
    }
}
