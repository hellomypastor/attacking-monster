package net.hellomypastor.java8.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

public class TestNewTime {
	@Test
	public void test1() {
		LocalDateTime time = LocalDateTime.now();

		System.out.println(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
}
