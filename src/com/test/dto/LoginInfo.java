package com.test.dto;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginInfo {

	private String id;
	private String pw;
	private Map<String,String> extraInfo;

}
