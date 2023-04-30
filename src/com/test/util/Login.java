package com.test.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {

	private WebDriver driver;
	private WebDriverWait waiter;
	private WebElement webElement;
	private String id;
	private String pw;

	public Login(WebDriver driver, WebDriverWait waiter, String id, String pw) {
		super();
		this.driver = driver;
		this.waiter = waiter;
		this.id = id;
		this.pw = pw;
	}

}
