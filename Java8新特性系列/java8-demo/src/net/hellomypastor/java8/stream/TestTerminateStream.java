package net.hellomypastor.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

import net.hellomypastor.java8.entity.Person;

/**
 * 终止操作：
 * 1、查找与匹配
        allMatch(Predicate p):检查是否匹配所有元素
        anyMatch(Predicate p)：检查是否至少匹配一个元素
        noneMatch(Predicate p)：检查是否没有匹配任何元素
        findFirst():返回第一个元素
        findAny()：返回任意一个元素
        count():返回流中元素的总数
        max(Comparator c):返回流中的最大值
        min(Comparator c):返回流中的最小值
        forEach(Consumer c):迭代

   2、归约
        reduce(T iden,BinaryOperator b):可以将流中的元素反复结合起来，得到一个值，返回T
        reduce(BinaryOperator b):可以将流中的元素反复结合起来，得到一个值，返回Optional<T>

   3、收集
        collect(Collector c)：将流转换为其他形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
                                Collectors工具类提供了很多实用静态方法
 */
public class TestTerminateStream
{

    @Test
    public void testFindAndMatch()
    {
        List<String> strs = Arrays.asList("aaa", "bbb", "ddd", "ttt", "eee", "cccc");

        //allMatch(Predicate p):检查是否匹配所有元素
        boolean allMatch = strs.stream().allMatch((str) -> str.length() == 3);
        System.out.println(allMatch);

        //anyMatch(Predicate p)：检查是否至少匹配一个元素
        boolean anyMatch = strs.stream().anyMatch((str) -> str.length() == 3);
        System.out.println(anyMatch);

        //noneMatch(Predicate p)：检查是否没有匹配任何元素
        boolean noneMatch = strs.stream().noneMatch((str) -> str.length() == 5);
        System.out.println(noneMatch);

        //findFirst():返回第一个元素
        Optional<String> findFirst = strs.stream().findFirst();
        System.out.println(findFirst.get());

        //findAny()：返回任意一个元素
        Optional<String> findAny = strs.parallelStream().findAny();
        System.out.println(findAny.get());

        //count():返回流中元素的总数
        long count = strs.stream().count();
        System.out.println(count);

        //max(Comparator c):返回流中的最大值
        Optional<String> max = strs.stream().max(String::compareTo);
        System.out.println(max.get());

        //min(Comparator c):返回流中的最小值
        Optional<String> min = strs.stream().min(String::compareTo);
        System.out.println(min.get());

        //forEach(Consumer c):迭代
        strs.stream().forEach(System.out::println);
    }

    //reduce(T iden,BinaryOperator b):可以将流中的元素反复结合起来，得到一个值，返回T
    //reduce(BinaryOperator b):可以将流中的元素反复结合起来，得到一个值，返回Optional<T>
    @Test
    public void test2()
    {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Integer sum = nums.stream().reduce(0, (x, y) -> x + y);
        System.out.println(sum);

        Optional<Integer> sumOptional = nums.stream().reduce((x, y) -> x + y);
        System.out.println(sumOptional.get());
    }

    //collect(Collector c)：将流转换为其他形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
    //Collectors工具类提供了很多实用静态方法
    @Test
    public void testCollect()
    {
        List<Person> persons = Arrays.asList(new Person("Tom", 24, "南京"), new Person("Jim", 25, "北京"),
                new Person("Lucy", 28, "南京"), new Person("Jack", 27, "西安"));

        List<Person> nanJingPersons = persons.stream().filter(person -> person.getCity().equals("南京"))
                .collect(Collectors.toList());
        System.out.println(nanJingPersons.size());

        List<String> strs = Arrays.asList("aaa", "bbb", "ddd", "aaa", "ccc", "ttt", "eee", "ccc");
        strs.stream().collect(Collectors.toSet()).forEach(System.out::println);

        Map<String, List<Person>> map = persons.stream().collect(Collectors.groupingBy(Person::getCity));
        System.out.println(map);
    }
}
