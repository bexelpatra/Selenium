package com.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

// import io.github.bonigarcia.wdm.WebDriverManager;

public class OpenPage {
	private WebDriver driver;
	private JavascriptExecutor js;
	private WebDriverWait waiter;

	private WebElement webElement;
	private String base_url;
	private Map<String, String> propertiesMap;

	private String saveDir = "src/images/";

	public OpenPage(String base_url) throws Exception {

		super();
//		WebDriverManager.chromedriver().setup();
		
		System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
		// System.out.printf("chrome driver path : %s by webDriverManager \n",WebDriverManager.chromedriver().getDownloadedDriverPath());
		// os 에 따른 크롬드라이버 선택

		this.base_url = base_url;
		ChromeOptions options = new ChromeOptions();

		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability("acceptInsecureCerts", true);
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
		waiter = new WebDriverWait(driver, Duration.of(3000, ChronoUnit.MILLIS));
		js = (JavascriptExecutor) driver;
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	BufferedReader reader;
	public void open() throws Exception{
		driver.get(base_url);
		String line = reader.readLine();
	}
}
