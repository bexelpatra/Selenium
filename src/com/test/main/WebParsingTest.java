package com.test.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.util.ImageMerge;

import io.github.bonigarcia.wdm.WebDriverManager;

// 텔레그램 메세지 파싱
// chat bot api도 있지만 추후 도전해보기로...
public class WebParsingTest {
	private WebDriver driver;
	private JavascriptExecutor js;
	private WebDriverWait waiter;

	private WebElement webElement;
	private Actions action;

	// 크롤링 할 URL
	private String base_url;
	public static void main(String[] args) throws Exception{
//		filereader();
		WebParsingTest p = new WebParsingTest();
		p.doJob();

	}
	public String getParam(String text,String name,int len) {
		
		int start = text.indexOf(name);
		if(start<0) return "";
		start+=name.length()+3;
		int end = text.indexOf(name)+name.length()+3+len;
		return text.substring(start,end);
	}
	public WebParsingTest() {
		// TODO Auto-generated constructor stub
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		// 리눅스에서 실행하기 위해 필요한 옵션들...		
		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability("acceptInsecureCerts", true);
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
		waiter = new WebDriverWait(driver, Duration.of(2000, ChronoUnit.MILLIS));
		js = (JavascriptExecutor) driver;
//		base_url = "chrome://version/";
		base_url = "https://comic.naver.com/webtoon/detail?titleId=778963&no=93";
		action = new Actions(driver);
		driver.get(base_url);
//		System.out.println(driver.findElement(By.cssSelector("#copy-content")).getText());
		
	}
	public void doJob() {
		// webElement를 이용해서 진행한다. webparsing으로 접근하지만 속도가 상당히 느리다
//		byWebElement();
		// 문자열로 다룬다. 속도가 훨씬 빠르다.
//		byInnerText();
		driver.findElement(By.cssSelector("body")).click();
		
		action
		.keyDown(Keys.F12)
		.build().perform();
		
		action = new Actions(driver);
		action
		.keyDown(Keys.CONTROL)
		.keyDown(Keys.LEFT_SHIFT)
//		.keyDown(Keys.get))
		.build().perform();
		
		
	}
	public void doJob2() {
		ImageMerge imageMerge = new ImageMerge(driver); 		
	}
	private void byInnerText() {
		String totalText = driver.findElement(By.className("history")).getText();
		System.out.println(totalText.length());
		int i=0;
		int j=0;
		int resultStart = 0;
		int dateStart = 0;
		String resultCode = "";
		String date = "";
		while((resultStart = totalText.indexOf("resultCode",i))>-1) {
			dateStart = totalText.indexOf("Generation time",j);
			
			resultCode = totalText.substring(resultStart+13, resultStart+17);
			date = totalText.substring(dateStart+17, dateStart+37);
			
			i =resultStart+1;
			j= dateStart+1;
			System.out.printf("resultCode : %s, date : %s\n",resultCode,date);
		}
		
		
	}
	private void byWebElement() {
		List<WebElement> list = driver.findElements(By.className("message"));
		Map<String,Map<String,Integer>> map = new HashMap<>();
		list.stream().filter(t -> t.getDomAttribute("class").contains("default")).forEach(t -> {
			String klass = t.getDomAttribute("class");
			String id= t.getDomAttribute("id");
			try {
				String message = "";
				if(klass.contains("joined")) {
					message = t.findElement(By.xpath("//*[@id='"+ id+"']/div/div[2]")).getText();
				}else {
					message = t.findElement(By.xpath("//*[@id='"+ id+"']/div[2]")).getText();
				}
//				System.out.println(message);
				String resultCode = getParam(message, "resultCode", 4);
				String dateTime =getParam(message, "Generation time", 19); 
				if("".equals(resultCode)) {
					return;
				}
//				System.out.print(resultCode+"\t");
//				System.out.print(dateTime);
//				System.out.println();
				Map<String,Integer> tmap = map.getOrDefault(dateTime.split(" ")[0], new HashMap<String, Integer>());
				if(tmap.get(resultCode)==null) {
					tmap.put(resultCode, 1);
				}else {					
					tmap.put(resultCode, tmap.get(resultCode)+1);
				}
				map.put(dateTime.split(" ")[0], tmap);
//				System.out.println(t.findElement(By.cssSelector("#"+id+" > div > div.text > strong:nth-child(2)")).getText());				
				////*[@id="message87"]/div/div[2]/text()[4]
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		});;
		
		Object[] tempArr = map.keySet().toArray();
		Arrays.sort(tempArr);
		for (Object m : tempArr) {
			System.out.print(m);
			Map<String,Integer> k = map.get(m);
			
			System.out.printf("\t%d \t%d \t%d \t%d"
							,k.getOrDefault("2001",0)
							,k.getOrDefault("2008",0)
							,k.getOrDefault("3011",0)
							,k.getOrDefault("3012",0));
					
			System.out.println();
		}
	}
	private static void filereader() throws FileNotFoundException, IOException, UnsupportedEncodingException {
		FileInputStream reader = new FileInputStream(new File("C:/Temp/tel/messages2.html"));
		BufferedReader br = new BufferedReader(new InputStreamReader(reader));
		String line = null;
		while((line= br.readLine())!=null) {
			System.out.println(new String(line.getBytes(),"utf-8"));
		}
	}
}
