package com.test.website;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.test.dto.LoginInfo;
import com.test.interfaces.FilenameSetter;
import com.test.interfaces.WebPageLoading;
import com.test.interfaces.WebPageLogin;
import com.test.util.ImageMerge;
import com.test.util.MyUtils;

public class Webtoon  implements WebPageLoading ,FilenameSetter{
    WebDriver driver;
    JavascriptExecutor js;

    LoginInfo loginInfo;
    
    public Webtoon(WebDriver webDriver,LoginInfo loginInfo) {
        this.driver = webDriver;
        this.js = (JavascriptExecutor)webDriver;
        this.loginInfo = loginInfo;
    }

    @Override
    public List<String> loading(WebDriver driver) {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<>();

        for (int i = 170; i < 182; i++) {
            
            list.add(String.format("window.open('https://comic.naver.com/webtoon/detail?titleId=739115&no=%d&week=finish')", i));
        }

        return list;
    }

    public static void main(String[] args) {

    String webUrl = "https://comic.naver.com/webtoon/detail?titleId=739115&no=1&week=finish";
    WebDriver driver = MyUtils.getWebDriver();
    driver.get(webUrl);
    Webtoon webtoon = new Webtoon(driver, null);

    String mainwindow = driver.getWindowHandle();
    // driver.close();

    List<String> list = webtoon.loading(driver);
    ImageMerge im = new ImageMerge(driver, "src/images/test/", webtoon);

    im.asyncSaveImage(mainwindow,driver2 ->{
        int count =0;
        while(driver2.findElement(By.cssSelector("#content")).getSize().getHeight()<1500 && count < 3){
            count+=1;
            MyUtils.sleep(1000);
            System.out.println("대기중");
        }
        return true;
    }, list);

    // im.saveImage(mainwindow,driver2 ->{
    //     int count =0;
    //     while(driver2.findElement(By.cssSelector("#content")).getSize().getHeight()<1500 || count < 3){
    //         count+=1;
    //         MyUtils.sleep(200);
    //     }
    //     return true;
    // }, list);
    // for (List<String> urls : MyUtils.divideList(list, 1)) {
    //     Thread thread = new Thread(() -> {
    //         WebDriver innerDriver = MyUtils.getWebDriver();
    //         innerDriver.get("https://www.google.com/");
    //         String innerMain = innerDriver.getWindowHandle();
    //         ImageMerge im = new ImageMerge(innerDriver, "src/images/test/",webtoon);
    //         im.saveImage(innerMain,driver2 ->{return true;} ,urls );
    //         // im.asyncSaveImage(innerMain, urls);
    //     });
    //     thread.start();
    // }
    }

    @Override
    public String setFileName(WebDriver webdriver) {
            String name = webdriver.findElement(By.cssSelector("#wrap > div > div > div >div:nth-child(1) > a>strong")).getText();
            String nth = webdriver.findElement(By.cssSelector("#wrap > div > div > div >div:nth-child(2) > span")).getText();
            ((JavascriptExecutor)webdriver).executeScript("document.querySelector(\"#wrap > div.viewer_toolbar_wrap.is_active\").setAttribute(\"hidden\",true)");
            return name + "_"+nth;
    }
    
}
