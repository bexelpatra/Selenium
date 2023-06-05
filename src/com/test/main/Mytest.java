package com.test.main;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Mytest {

	public static void main(String[] args) {

		ZonedDateTime t = ZonedDateTime.parse("2023-01-21 10:46:09"+" KST",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
//		System.out.println(temp.substring(-1, 100));
		System.out.println(t.toString());
		String a = "[ 처리일시 : \"2023-04-14 15:55\" ] ";
		String b= "20230414";
		System.out.println(String.format("%-12s", b));
		int e= 123;
		System.out.println(String.format("%-12d", e));
		String regex = "[^0-9]";
		a= a.replaceAll(regex, "");
		System.out.println(a);
		
		System.out.println(paddingRight(b,"0", a.length()-b.length()));
		
		System.out.println((int)'p');
		StringBuilder sb = new StringBuilder();
		
		
		System.out.println(new String("\u0070"));
		System.out.println("A".codePointAt(0));
		System.out.println("a".codePointAt(0));
	}
	
	public static String paddingRight(String text, String pad, int count) {
		StringBuilder sb = new StringBuilder(text);
		for (int i = 0; i < count; i++) {
			sb.append(pad);
		}
		return sb.toString();
	}
	public static String paddingLeft(String text, String pad, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count-text.length(); i++) {
			sb.append(pad);
		}
		sb.append(text);
		return sb.toString();
	}

}
