package com.test.main;

public class Mytest {

	public static void main(String[] args) {
		String c9 = "fnView('12652395','C0007000400000000');";
		int a= c9.indexOf('\'');
		int b=c9.indexOf('\'',a+1);

		System.out.println(a);
		
		System.out.println(b);
		System.out.println(c9.substring(a+1,b));
	}
}
