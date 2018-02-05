package net.hellomypastor.java8.entity;

/**
 * @author shipeipei 实体类
 *
 */
public class Person {
	private String name;
	private int age;
	private String city;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Person(String name, int age, String city) {
		this.name = name;
		this.age = age;
		this.city = city;
	}

	public Person(String name) {
		this.name = name;
	}

	public Person() {
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", city=" + city + "]";
	}

}
