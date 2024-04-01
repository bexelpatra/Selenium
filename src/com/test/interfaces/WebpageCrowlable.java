package com.test.interfaces;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.test.dto.LoginInfo;
import com.test.util.ImageMerge;

public abstract class WebpageCrowlable implements WebPageLogin,WebPageLoading{
    WebDriver driver;
    JavascriptExecutor js;
    ImageMerge imageMerge;
    
}
