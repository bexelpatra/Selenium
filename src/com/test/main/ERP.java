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

	// ũ�Ѹ� �� URL
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
		// ���������� �����ϱ� ���� �ʿ��� �ɼǵ�...
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
			// get page (= ���������� url�� �ּ�â�� ���� �� request �� �Ͱ� ����)
			// 1. login
			login(propertiesMap.get("userid"),propertiesMap.get("password"));
			Thread.sleep(500);
			
			// 2. ���� ������ �̵� 
			driver.get(base_url + "TR/TMFBDM00200");
			Thread.sleep(2500);
			
			// 2 - 1 �˾� ����
			driver.findElement(By.id("notice-dialog-close")).click();
			;

			// 2 - 2 ī���ȣ ã�� �˻� �� ����
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

			// 2 - 3 ������ �����ϱ�
			LocalDate startinput = LocalDate.of(Integer.parseInt(propertiesMap.get("year")), Integer.parseInt(propertiesMap.get("month")),1);
			driver.findElement(By.xpath("//*[@id=\"TRAN_DT_C_startinput\"]")).sendKeys(startinput.toString());
			LocalDate endinput = startinput.plusMonths(1).minusDays(1); // ���� ���
			driver.findElement(By.xpath("//*[@id=\"TRAN_DT_C_endinput\"]")).sendKeys(endinput.toString());

			// 2 - 4 ȸ���� ó���ϱ�(�������� �Ѵ�.)
			driver.findElement(By.xpath("//*[@id=\"li-ACTG_DT_H\"]/span/span/span/span")).click();
			Period period = Period.between(LocalDate.now(), endinput);
			Keys arrows = period.isNegative() ? Keys.ARROW_LEFT : Keys.ARROW_RIGHT;

			for (int i = 0; i < Math.abs(period.getDays()); i++) {
				action.keyDown(arrows);
			}
			action
				.keyDown(Keys.ENTER)
				.build().perform();

			// 2 - 5 �˻���ư Ŭ��
			action
				.keyDown(Keys.F2)
				.build().perform();
			Thread.sleep(1000);
			
			// 3. ��ħ�Ĵ� �Է��ϱ�
			js.executeScript("var myGrid ;");
			Thread.sleep(50);
//			js.executeScript("var fieldFinder ;");
//			js.executeScript("var titleFinder ;");

			js.executeScript("$(\"#TMFBDM00200_1000_4000_grid\").one('changing',function(event, data) {"
					+ "                myGrid = event.grid ;"
					+ "                });");


			
			// �����ڰ� �÷��˻��ϱ� ���ؼ� �߰�
//			js.executeScript("fieldFinder = (f)=>{return myGrid.options.columns.filter((e)=>{return e.field== f ;})[0];}");
//			js.executeScript("titleFinder = (f)=>{return myGrid.options.columns.filter((e)=>{return e.title== f ;})[0];}");

			// 3 - 1. changing �̺�Ʈ �߻���Ű�� -> ���� �Լ��� �ܺη� ������ ���� �ʼ�
			webElement = driver.findElement(By.xpath("//*[@id=\"TMFBDM00200_1000_4000_grid\"]/div/canvas"));
			webElement.click();
			action
				.keyDown(Keys.TAB)
				.keyDown(Keys.ARROW_RIGHT)
				.build()
				.perform();
			

			// 3 - 2. ������ ������ �������
			String dataListStr =(String) js.executeScript("return JSON.stringify(myGrid.options.data)"); 

			/**
			 *  3 - 3. ������ �Է� �� �����ϱ�
			 *  - ���ϴ� ������ �Է��ؼ� �پ��ϰ� �ڵ��Է��� ��ų �� �ִ� �����Դϴ�.
			 *  -  ���� �ڵ�, ���� �̸�, ���ڵ�, ���̸�, ������Ʈ �ڵ�, ������Ʈ �̸�
			 */
			gridRow = new GridRow(js, propertiesMap.get("BG_CD"), propertiesMap.get("BG_NM")
									, propertiesMap.get("CC_CD"), propertiesMap.get("CC_NM")
									, propertiesMap.get("PJT_CD"), propertiesMap.get("PJT_NM"));
			JSONArray paymentList = (JSONArray) new JSONParser().parse(dataListStr);

			for (int i = 0; i < paymentList.size(); i++) {
				Map<String, Object> paymentRow = (Map)paymentList.get(i);

				// �հ�κ� �Ѿ��
				if(paymentRow.get("CARD_NM")== null) {
					continue;
				}
				// �̹� �ۼ��� �κ� �Ѿ��
				if(paymentRow.get("NOTE_DC")!= null || "".equals(paymentRow.get("NOTE_DC"))) {
					continue;
				}
				
				// �������� ����Ҹ��� ����? ���ó, �ð�, �ݾ�
				String trab_nm = paymentRow.get("TRAN_NM").toString(); // ���ó
				int tran_amt = Integer.parseInt(paymentRow.get("TRAN_AMT").toString()); // �ݾ�
				int tran_tm = Integer.parseInt(paymentRow.get("TRAN_TM").toString()); // ��� �ð�
				
				if(tran_amt <= 5000 && tran_tm < 10_00_00 ) { // 5000�� ����, ���ð��� 10�� ����
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

	
	// �����Է°� �ڵ��Է¿��� �ٸ� ���� �ִ��� Ȯ���Ѵ�.
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
