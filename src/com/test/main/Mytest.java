package com.test.main;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Mytest {

	public static void main(String[] args) {

		ZonedDateTime t = ZonedDateTime.parse("2023-01-21 10:46:09"+" KST",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
		System.out.println(temp.substring(-1, 100));
		System.out.println(t.toString());
	}
}
