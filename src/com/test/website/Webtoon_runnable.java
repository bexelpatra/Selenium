package com.test.website;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.test.dto.LoginInfo;
import com.test.interfaces.FilenameSetter;
import com.test.interfaces.WebPageLoading;
import com.test.util.ImageMerge;
import com.test.util.MyUtils;

public class Webtoon_runnable  implements WebPageLoading ,FilenameSetter{
    WebDriver driver;
    JavascriptExecutor js;

    LoginInfo loginInfo;
    
    public Webtoon_runnable(WebDriver webDriver,LoginInfo loginInfo) {
        this.driver = webDriver;
        this.js = (JavascriptExecutor)webDriver;
        this.loginInfo = loginInfo;
    }

    @Override
    public List<String> loading(WebDriver driver) {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<>();

        for (int i = 2; i < 5; i++) {
            
            list.add(String.format("window.open('https://comic.naver.com/webtoon/detail?titleId=183559&no=%d')", i));
            // list.add(String.format("window.open('https://comic.naver.com/webtoon/detail?titleId=739115&no=%d&week=finish')", i));
        }

        return list;
    }

    public static void main(String[] args) {

        getMultiTreadYX();
        drivers.forEach(WebDriver::close);
    }
    static List<WebDriver> drivers = new ArrayList<>();
    private static void getMultiTreadYX() {
        String webUrl = "https://www.google.com/";
        WebDriver driver = MyUtils.getWebDriver();
        driver.get(webUrl);
        Webtoon webtoon = new Webtoon(driver, null);
        
        List<String> list = webtoon.loading(driver);
        driver.close();

        List<Thread> threads = new ArrayList<>();
        for (List<String> urls : MyUtils.divideList(list, 1)) {
            Runnable r = ()->{
                WebDriver innerDriver = MyUtils.getWebDriver();
                drivers.add(innerDriver);
                innerDriver.get("https://www.google.com/");
                String innerMain = innerDriver.getWindowHandle();
                ImageMerge im = new ImageMerge(innerDriver, "src/images/test/",webtoon);
                im.asyncSaveImageYX(innerMain,driver2 ->{return true;} ,urls ,By.cssSelector("#wrap"));
            };
            Thread thread = new Thread(r);
            // thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String setFileName(WebDriver webdriver) {
        StringBuilder sb = new StringBuilder();
        String name = webdriver.findElement(By.cssSelector("#wrap > div > div > div >div:nth-child(1) > a>strong")).getText();
        String nth = webdriver.findElement(By.cssSelector("#wrap > div > div > div >div:nth-child(2) > span")).getText();
        ((JavascriptExecutor)webdriver).executeScript("document.querySelector(\"#wrap > div.viewer_toolbar_wrap.is_active\").setAttribute(\"hidden\",true)");
        sb.append(name).append("_").append(nth);
        return sb.toString().replaceAll("[<>?*:|\"]"," ");
    }
    
}
