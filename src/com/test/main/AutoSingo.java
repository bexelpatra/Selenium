package com.test.main;

import com.test.util.Capture;

public class AutoSingo {
	// Properties
	public static void main(String[] args) throws Exception{
		
		System.out.println("================");
		System.out.println("job started");
		System.out.println("================");
		System.out.println();
		
		Capture c = new Capture("https://onetouch.police.go.kr/login.do",false);
		c.build("src/singo.properties")
		 .doJob();
//		c.imageSaveTest();
		
		System.out.println();
		System.out.println("================");
		System.out.println("job done");
		System.out.println("================");
	}
	
}
