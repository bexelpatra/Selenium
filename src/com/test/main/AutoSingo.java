package com.test.main;

import com.test.util.Capture;
import com.test.website.SmartJebo;

public class AutoSingo {
	// Properties
	public static void main(String[] args) throws Exception{
		// 이제 더이상 미사용
		
		System.out.println("================");
		System.out.println("job started");
		System.out.println("================");
		System.out.println();
	
		Capture c = new Capture("https://www.safetyreport.go.kr/#main/login/login",false);
		c.build("src/singo2.properties")
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
