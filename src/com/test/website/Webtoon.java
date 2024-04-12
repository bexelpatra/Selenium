package com.test.website;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        for (int i = 1; i < 431; i++) {
            
            list.add(String.format("window.open('https://comic.naver.com/webtoon/detail?titleId=648419&no=%d')", i));
            // list.add(String.format("window.open('https://comic.naver.com/webtoon/detail?titleId=739115&no=%d&week=finish')", i));
        }

        return list;
    }

    public static void main(String[] args) {

    // getAsyncSave();
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
        for (List<String> urls : MyUtils.divideList(list, 8)) {
            Runnable r = ()->{
                WebDriver innerDriver = MyUtils.getWebDriver();
                drivers.add(innerDriver);
                innerDriver.get("https://www.google.com/");
                String innerMain = innerDriver.getWindowHandle();
                ImageMerge im = new ImageMerge(innerDriver, "src/images/test/",webtoon);
                im.asyncSaveImageYX(innerMain,driver2 ->{return true;} ,urls );
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
    private static void getMultiTread() {
        String webUrl = "https://www.google.com/";
        WebDriver driver = MyUtils.getWebDriver();
        driver.get(webUrl);
        Webtoon webtoon = new Webtoon(driver, null);
        
        List<String> list = webtoon.loading(driver);
        driver.close();

        List<Thread> threads = new ArrayList<>();
        for (List<String> urls : MyUtils.divideList(list, 2)) {
            Runnable r = ()->{
                WebDriver innerDriver = MyUtils.getWebDriver();
                drivers.add(innerDriver);
                innerDriver.get("https://www.google.com/");
                String innerMain = innerDriver.getWindowHandle();
                ImageMerge im = new ImageMerge(innerDriver, "src/images/test/",webtoon);
                im.asyncSaveImage(innerMain,driver2 ->{return true;} ,urls );
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

    private static void getAsyncSave() {
        String webUrl = "https://www.google.com/";
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
            }
            return true;
        }, list);
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
