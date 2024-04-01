package com.test.main;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.test.dto.LoginInfo;
import com.test.util.ImageMerge;
import com.test.util.MyUtils;
import com.test.website.Sinmungo;

public class AutoSingo {
    public static void main(String[] args) throws Exception {
        System.out.println("================");
		System.out.println("job started");
		System.out.println("================");
		System.out.println();
        String[] propertyNames = new String[]{"src/singo2.properties","src/singo.properties"};
        Map<String,String> propertiesMap = MyUtils.loadProperties("src/singo2.properties");
        WebDriver driver = MyUtils.getWebDriver();
        
        
        driver.get("https://www.safetyreport.go.kr/#main/login/login");
        LoginInfo loginInfo = LoginInfo.builder()
                                        .id(propertiesMap.get("userid"))
                                        .pw(propertiesMap.get("password"))
                                        .build();
		
        Sinmungo sinmungo = new Sinmungo(driver);
        sinmungo.login(driver, loginInfo);
        // 로그인 완료

		driver.get("https://www.safetyreport.go.kr/#/mypage/mysafereport");
        sinmungo.waiter(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#table1Body")));
        List<String> addresses = sinmungo.loading(driver);

        String mainWindow = driver.getWindowHandle();
		ImageMerge imageSaver = new ImageMerge(driver,"src/images/test/", webdriver -> {
			String fileName = "default";
			try {
				fileName = webdriver.findElement(By.cssSelector("#contents > div:nth-child(4) > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong")).getText();
			} catch (Exception e) {
				// TODO: handle exception
				fileName="fail to get title";
			}
			return fileName;
		});

        imageSaver.saveImage(mainWindow, webdriver->{
            for (int j = 0; j < 3; j++) {
                int h =driver.findElement(By.xpath("/html/body")).getSize().getHeight();
                if(h>1000){
                    return true;
                } else{
                    MyUtils.sleep(1000);
                }
            }
            return false;
        },addresses);

		System.out.println();
		System.out.println("================");
		System.out.println("job done");
		System.out.println("================");
    }
}
