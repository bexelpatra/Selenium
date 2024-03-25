package com.test.interfaces;

import java.util.List;

import org.openqa.selenium.WebDriver;

public interface WebPageLoading {
    // 조건을 걸어서 페이지를 걸러내서 띄울 화면들만 모아낸다.
    List<String> loading(WebDriver driver);
}
