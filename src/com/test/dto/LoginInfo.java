package com.test.dto;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginInfo {

	private final WebDriver driver;
	private final By byId;
	private final By byPw;
	private final String id;
	private final String pw;

	private final By byButton;
	
	
	public void login() {
		driver.findElement(byId).sendKeys(id);
		driver.findElement(byPw).sendKeys(pw);
		driver.findElement(byButton).click();
	}
	
}
