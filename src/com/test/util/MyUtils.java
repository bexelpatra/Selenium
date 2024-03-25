package com.test.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.netty.util.ThreadDeathWatcher;

public class MyUtils {

	public static WebDriver getWebDriver(){
		System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability("acceptInsecureCerts", true);
		options.addArguments("--start-maximized");
		
		return new ChromeDriver(options);
	}
	public static Date toDate(SimpleDateFormat format,String str) throws ParseException {
		return format.parse(str);
	}
	
	public static Map<String,String> loadProperties(String fileUrl) throws Exception {
		Properties properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream(new File(fileUrl))));
		
		Map<String,String> propertiesMap = new HashMap<>();

		System.out.println();
		System.out.println("\t\tchecking your properties file\n");
		for (Object ob : properties.keySet()) {
			System.out.println("\t\t" + ob + " : \t "
					+ new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
			propertiesMap.put((String) ob,
					new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
		}
		return propertiesMap;
	}
	private String convertEncoding(String iso) {
		String a = "";
		try {
			a = new String(iso.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public static void sleep(long mil) {
		try {
			Thread.sleep(mil);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
