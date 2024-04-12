package com.test.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.google.common.collect.Queues;

public class MyUtils {

	public static WebDriver getWebDriver(){
		System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability("acceptInsecureCerts", true);
		options.addArguments("--start-maximized");
		

		options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
		options.addArguments("--headless"); // only if you are ACTUALLY running headless
		options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
		options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
		options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
		options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
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

	public static void deleteFiles(File[] files){
		Arrays.stream(files).filter(t -> {
			return t!=null;
		}).forEach(File::deleteOnExit);
	}
	public static void deleteFiles(File[][] file2d){
		for (File[] files : file2d) {
			Arrays.stream(files).filter(t -> {
				return t!=null;
			}).forEach(File::deleteOnExit);
		}
	}
	public static List<List<String>> divideList(List<String> list, int n ){
		if(n<=0) n =1;
		List<List<String>> result = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			result.add(new ArrayList<>());
		}
		int size = list.size();
		int num=0;
		a:while(num < size){
			for (int i = 0; i < n; i++) {
				result.get(i).add(list.get(num++));
				if(num == size) {
					break a;
				}
			}
		}
		return result;
	}
	
	public static void copy(FileInputStream is, FileOutputStream os) {
		byte[] b = new byte[1024];
		int readData = 0;
		try {
			while((readData = is.read(b))>0) {
				os.write(b, 0, readData);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				is.close();
				os.close();
			} catch (Exception e2) {
				// TODO: handle exception
				System.out.println("close fail!");
			}
		}
	}

	public static BufferedImage getBufferedImage(File file) {
		BufferedImage tempImage=null;
		try {
			tempImage = ImageIO.read(file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("image read fail");
		}
		return tempImage;
	}
}
