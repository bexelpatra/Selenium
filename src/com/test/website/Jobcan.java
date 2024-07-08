package com.test.website;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.dto.LoginInfo;
import com.test.interfaces.WebPageLogin;
import com.test.util.MyUtils;
import com.test.util.WebDriverManager;

public class Jobcan implements WebPageLogin {
    WebDriver driver;
    JavascriptExecutor js;

    WebDriverWait waiter;
    
    String chromePath;
    public Jobcan(WebDriver webDriver) {        
        this.driver = webDriver;
        this.js = (JavascriptExecutor)webDriver;
        this.waiter = new WebDriverWait(driver, Duration.of(3000, ChronoUnit.MILLIS));

    }
    public static void main(String[] args) throws Exception {

        String properiesPath = "d:/dot.properties";
        ZonedDateTime now = ZonedDateTime.now();
        
        System.out.println("=================================================================");
        System.out.println("Job Started!!!     "+now.toString());
        
        String today = now.format(DateTimeFormatter.ofPattern("MMdd"));
        Map<String, String> map = MyUtils.loadProperties(properiesPath);
        
        String[] holidays = map.getOrDefault("holiday","").replaceAll(" ", "").split(","); // 공백 제거 후 쪼갬

        for (String holiday : holidays) {
            if(today.equals(holiday)){
                System.out.println("today is happy holiday");
                return;
            }
        }
        if(true){
            return;
        }
        
        WebDriverManager manager = new WebDriverManager();
        
        manager.setChromePath(); // default path : C:/Program Files/Google/Chrome/Application
        manager.downloadDriver(); // default path : D:/chromeDriver/{yyyyMMddhhmmss}/
        
        Jobcan jobcan = new Jobcan(MyUtils.getWebDriver(false));
     
        String id = map.get("id");
        String pw = map.get("pw");
        
        LoginInfo loginInfo = new LoginInfo(id, pw, null);
        
        // random timer
        Random random = new Random();
        
        int sleep = (random.nextInt(300)+1) * 1000;
        // MyUtils.sleep(sleep);
        System.out.println(sleep);
        jobcan.excute(loginInfo);
        
        System.out.println("=================================================================\n");
    }

    public void excute(LoginInfo loginInfo){
        driver.get("https://id.jobcan.jp/users/sign_in?lang=ko");
        driver.manage().window().maximize();
        login(driver, loginInfo);

        // 체크인/아웃 페이지 이동
        driver.findElement(By.cssSelector("#jbc-app-links > ul > li:nth-child(3) > a")).click();

        String mainWindow = driver.getWindowHandle();
        for (String window : driver.getWindowHandles()) {
            if(mainWindow.equals(window))continue;
            driver.switchTo().window(window);
            MyUtils.sleep(3000);
            String time = driver.findElement(By.cssSelector("#clock")).getText();
            int i = 0;
            while("00:00:00".equals(time) && i++ < 3){
                time = driver.findElement(By.cssSelector("#clock")).getText();
                MyUtils.sleep(1000);
            }
            System.out.println(time);
            WebElement registerBtn = driver.findElement(By.cssSelector("#adit-button-push"));
            System.out.println(registerBtn.getText());
            // registerBtn.click();
        }

        // driver.quit();
    }
    @Override
    public void login(WebDriver driver, LoginInfo loginInfo) {
        // TODO Auto-generated method stub
        driver.findElement(By.cssSelector("#user_email")).sendKeys(loginInfo.getId());
        driver.findElement(By.cssSelector("#user_password")).sendKeys(loginInfo.getPw());
        driver.findElement(By.cssSelector("#login_button")).click();
        MyUtils.sleep(500);
    }
    
}
