package net.hellomypastor.java8.lambda;

/**
 * @author shipeipei
 *
 */
@FunctionalInterface
public interface PrintName {
	void print(String name);

	static void printAny(String str) {
		System.out.println(str);
	}

	default void appendAndPrint(String str1, String str2) {
		System.out.println(str1 + str2);
	}
}
