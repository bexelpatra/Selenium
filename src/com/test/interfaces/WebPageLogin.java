package com.test.interfaces;

import org.openqa.selenium.WebDriver;

import com.test.dto.LoginInfo;

public interface WebPageLogin {
    void login(WebDriver driver,LoginInfo loginInfo);
}
