package com.test.dto;

import java.util.Map;

public class LoginInfo {

	private String id;
	private String pw;
	private Map<String,String> extraInfo;
	public String getId() {
		return id;
	}
	public String getPw() {
		return pw;
	}
	public Map<String, String> getExtraInfo() {
		return extraInfo;
	}
	public LoginInfo(String id, String pw, Map<String, String> extraInfo) {
		this.id = id;
		this.pw = pw;
		this.extraInfo = extraInfo;
	}

	

}
