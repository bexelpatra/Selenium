package com.test.main;

import com.test.util.Capture;

public class AutoSingo {
	// Properties
	public static void main(String[] args) throws Exception{
		
		System.out.println("================");
		System.out.println("job started");
		System.out.println("================");
		System.out.println();
//		Capture c = new Capture("https://onetouch.police.go.kr/login.do",false);
		Capture c = new Capture("https://www.safetyreport.go.kr/#main/login/login",false);
		c.build("src/singo.properties")
		.doSinmungo();
		
		
//		c.imageSaveTest();
		
//		OpenPage op = new OpenPage("http://uracle.campus21.co.kr/main/");
//		op.open();
		
		
		
		System.out.println();
		System.out.println("================");
		System.out.println("job done");
		System.out.println("================");
	}
	
}
