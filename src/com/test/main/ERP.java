package com.test.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.util.GridRow;

public class ERP {

	public static void main(String[] args) throws Exception {
//		Properties properties = new Properties();
//		properties.load(new BufferedInputStream(new FileInputStream(new File("src/dot.properties"))));
//		
//		for (Object ob: properties.keySet()){
//			System.out.println(ob + ":"+new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
//		}
		System.out.println("================");
		System.out.println("job started");
		System.out.println("================");
		System.out.println();

		ERP erpInput = new ERP();			
		erpInput.GoodMorning();
		
		System.out.println();
		System.out.println("================");
		System.out.println("job done");
		System.out.println("================");

	}


	// WebDriver
	private WebDriver driver;
	private JavascriptExecutor js;
	private WebDriverWait waiter;

	private WebElement webElement;
	private Actions action;
	
	// Properties
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "src/chromedriver";

	// 크롤링 할 URL
	private String base_url;
	
	private GridRow gridRow;

	private Map<String,String> propertiesMap;
	
	public ERP() throws Exception {
		super();
		// os check
		String osName =System.getProperty("os.name").toLowerCase();
		if(osName!=null && osName.contains("windows")) {
			WEB_DRIVER_PATH+= ".exe"; 
		}else if(osName!=null && osName.contains("linux")) {
			WEB_DRIVER_PATH+= "Linux"; 			
		}
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		// 리눅스에서 실행하기 위해 필요한 옵션들...
		if(osName!=null && osName.contains("linux")) {			
			options.addArguments("--headless");		
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
		}
		
		
		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability("acceptInsecureCerts", true);
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
		waiter = new WebDriverWait(driver, Duration.of(1000, ChronoUnit.MILLIS));
		js = (JavascriptExecutor) driver;
		base_url = "https://erp.uracle.co.kr/";
		action = new Actions(driver);
		
		// properties setup
		propertiesLoad();
	}



	public void GoodMorning() {

		try {
			// get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
			// 1. login
			login(propertiesMap.get("userid"),propertiesMap.get("password"));
			Thread.sleep(500);
			
			// 2. 지출 페이지 이동 
			driver.get(base_url + "TR/TMFBDM00200");
			Thread.sleep(2500);
			
			// 2 - 1 팝업 끄기
			driver.findElement(By.id("notice-dialog-close")).click();
			;

			// 2 - 2 카드번호 찾기 검색 및 선택
			driver.findElement(By.xpath("//*[@id=\"validateForm_C\"]/form/ul[1]/li[3]/span/span/span/span/button[2]"))
					.click();
			Thread.sleep(1000);
			
			action
				.keyDown(Keys.TAB);
			for (int i = 0; i < Integer.parseInt(propertiesMap.getOrDefault("cardindex","0")); i++) {
				action.keyDown(Keys.ARROW_DOWN);
			}
			action
				.keyDown(Keys.SPACE)
				.keyDown(Keys.ENTER)
				.build()
				.perform();

			Thread.sleep(200);

			// 2 - 3 승인일 설정하기
			LocalDate startinput = LocalDate.of(Integer.parseInt(propertiesMap.get("year")), Integer.parseInt(propertiesMap.get("month")),1);
			driver.findElement(By.xpath("//*[@id=\"TRAN_DT_C_startinput\"]")).sendKeys(startinput.toString());
			LocalDate endinput = startinput.plusMonths(1).minusDays(1); // 월말 계산
			driver.findElement(By.xpath("//*[@id=\"TRAN_DT_C_endinput\"]")).sendKeys(endinput.toString());

			// 2 - 4 회계일 처리하기(계산해줘야 한다.)
			driver.findElement(By.xpath("//*[@id=\"li-ACTG_DT_H\"]/span/span/span/span")).click();
			Period period = Period.between(LocalDate.now(), endinput);
			Keys arrows = period.isNegative() ? Keys.ARROW_LEFT : Keys.ARROW_RIGHT;

			for (int i = 0; i < Math.abs(period.getDays()); i++) {
				action.keyDown(arrows);
			}
			action
				.keyDown(Keys.ENTER)
				.build().perform();

			// 2 - 5 검색버튼 클릭
			action
				.keyDown(Keys.F2)
				.build().perform();
			Thread.sleep(1000);
			
			// 3. 아침식대 입력하기
			js.executeScript("var myGrid ;");
			Thread.sleep(50);
//			js.executeScript("var fieldFinder ;");
//			js.executeScript("var titleFinder ;");

			js.executeScript("$(\"#TMFBDM00200_1000_4000_grid\").one('changing',function(event, data) {"
					+ "                myGrid = event.grid ;"
					+ "                });");


			
			// 개발자가 컬럼검색하기 위해서 추가
//			js.executeScript("fieldFinder = (f)=>{return myGrid.options.columns.filter((e)=>{return e.field== f ;})[0];}");
//			js.executeScript("titleFinder = (f)=>{return myGrid.options.columns.filter((e)=>{return e.title== f ;})[0];}");

			// 3 - 1. changing 이벤트 발생시키기 -> 내부 함수를 외부로 꺼내기 위한 초석
			webElement = driver.findElement(By.xpath("//*[@id=\"TMFBDM00200_1000_4000_grid\"]/div/canvas"));
			webElement.click();
			action
				.keyDown(Keys.TAB)
				.keyDown(Keys.ARROW_RIGHT)
				.build()
				.perform();
			

			// 3 - 2. 수정할 데이터 끌어오기
			String dataListStr =(String) js.executeScript("return JSON.stringify(myGrid.options.data)"); 

			/**
			 *  3 - 3. 데이터 입력 및 수정하기
			 *  - 원하는 조건을 입력해서 다양하게 자동입력을 시킬 수 있는 지점입니다.
			 *  -  본부 코드, 본부 이름, 팀코드, 팀이름, 프로젝트 코드, 프로젝트 이름
			 */
			gridRow = new GridRow(js, propertiesMap.get("BG_CD"), propertiesMap.get("BG_NM")
									, propertiesMap.get("CC_CD"), propertiesMap.get("CC_NM")
									, propertiesMap.get("PJT_CD"), propertiesMap.get("PJT_NM"));
			JSONArray paymentList = (JSONArray) new JSONParser().parse(dataListStr);

			for (int i = 0; i < paymentList.size(); i++) {
				Map<String, Object> paymentRow = (Map)paymentList.get(i);

				// 합계부분 넘어가기
				if(paymentRow.get("CARD_NM")== null) {
					continue;
				}
				// 이미 작성된 부분 넘어가기
				if(paymentRow.get("NOTE_DC")!= null || "".equals(paymentRow.get("NOTE_DC"))) {
					continue;
				}
				
				// 조건으로 사용할만한 내용? 사용처, 시간, 금액
				String trab_nm = paymentRow.get("TRAN_NM").toString(); // 사용처
				int tran_amt = Integer.parseInt(paymentRow.get("TRAN_AMT").toString()); // 금액
				int tran_tm = Integer.parseInt(paymentRow.get("TRAN_TM").toString()); // 사용 시간
				
				if(tran_amt <= 5000 && tran_tm < 10_00_00 ) { // 5000원 이하, 사용시간이 10시 이전
					System.out.println(String.format("%d. %s : %s => %d ", i,paymentRow.get("TRAN_TM"),paymentRow.get("TRAN_NM"),tran_amt));
					gridRow.breakfast(i);
				}
				
			}
			
			Thread.sleep(100);
			action.keyDown(Keys.F7)
			.build().perform();
			
			try {
				Thread.sleep(100);
				webElement = driver.findElement(By.xpath("//*[@id=\"dews-msgbox-confirm\"]/div/div/div[2]/button[1]"));
				waiter.until(ExpectedConditions.elementToBeClickable(webElement));
				webElement.click();				
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

//            driver.close();
		}

	}

	
	// 수동입력과 자동입력에서 다른 값이 있는지 확인한다.
	private void compareValueTEST(JSONArray paymentList) {
		Map<String, Object> paymentRow0 = (Map)paymentList.get(0);
		Map<String, Object> paymentRow1 = (Map)paymentList.get(1);
		for (String key : paymentRow0.keySet()) {
			if(paymentRow0.get(key)!= null || (paymentRow1.get(key)!=null)) {
				if(key.equals("TRAN_AMT")) {
					System.out.println(paymentRow0.get(key));
				}
				if(!paymentRow0.get(key).equals(paymentRow1.get(key))) {						
					System.out.println(String.format("key : %s, row0 : %s, row1 : %s",key,paymentRow0.get(key),paymentRow1.get(key)));
				}
			}
		}
	}

	private void login(String id, String pw) throws InterruptedException {
		driver.get(base_url);
		waiter.until(ExpectedConditions.elementToBeClickable(By.tagName("button")));

		webElement = driver.findElement(By.id("userid"));
		webElement.sendKeys(id.toUpperCase());

		webElement = driver.findElement(By.id("password"));
		webElement.sendKeys(pw);

		webElement = driver.findElement(By.id("btn_login"));
		webElement.submit();
	}
	
	private void propertiesLoad() throws IOException, FileNotFoundException, UnsupportedEncodingException {
		Properties properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream(new File("src/dot.properties"))));

		if(propertiesMap == null) {
			propertiesMap = new HashMap<>();
		}
		
		System.out.println();
		System.out.println("\t\tchecking your properties file\n");
		for (Object ob: properties.keySet()){
			System.out.println("\t\t"+ob + " : \t " + new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
			propertiesMap.put((String) ob, new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
		}
	}	

}
