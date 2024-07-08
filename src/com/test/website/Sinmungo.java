package com.test.website;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.File;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.dto.LoginInfo;
import com.test.interfaces.FilenameSetter;
import com.test.interfaces.WebPageLoading;
import com.test.interfaces.WebPageLogin;
import com.test.util.ImageMerge;
import com.test.util.MyUtils;

// 안전 신문고 앱에 캡쳐기능이 있어서 필요성이 많이 줄었다...
public class Sinmungo implements WebPageLogin,WebPageLoading, FilenameSetter {

    WebDriver driver;
    JavascriptExecutor js;

    WebDriverWait waiter;
    
    public Sinmungo(WebDriver webDriver) {
        this.driver = webDriver;
        this.js = (JavascriptExecutor)webDriver;
        this.waiter = new WebDriverWait(driver, Duration.of(3000, ChronoUnit.MILLIS));

    }

    @Override
    public void login(WebDriver driver, LoginInfo loginInfo) {
        // TODO Auto-generated method stub
        driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(loginInfo.getId());
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(loginInfo.getPw());
		driver.findElement(By.xpath("//*[@id=\"contents\"]/div/ul/li[1]/article/div[1]/p[3]/button")).click();
		MyUtils.sleep(500);
    }

    public<T> void waiter(ExpectedCondition<T> condition) throws Exception{
        waiter.until(condition);
        MyUtils.sleep(500);
    }

    @Override
    public List<String> loading(WebDriver driver) {

        // 날짜 지정(오늘 날짜 기준 1년 전까지)
        String date = ZonedDateTime.now().minusYears(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        js.executeScript(String.format("document.querySelector('#C_FRM_DATE').value = '%s'", date));
        driver.findElement(By.cssSelector("#form > fieldset > div > div:nth-child(2) > button")).click();
        MyUtils.sleep(1000);

        // TODO Auto-generated method stub
        List<String> list = new ArrayList<>();
        int count = Integer.parseInt(driver.findElement(By.cssSelector("#contents > div.table_bbs.list.tb_sty01 > p > strong")).getText());        

        int loop = count/10 + (count %10>0?1:0);
        System.out.println(loop);
        WebElement nextButton = driver.findElement(By.cssSelector("#table1 > tfoot > tr > td > ul > li:nth-last-child(2) > a"));
        for (int i = 0; i < loop; i++) {
            MyUtils.sleep(300);
            nextButton = driver.findElement(By.cssSelector("#table1 > tfoot > tr > td > ul > li:nth-last-child(2) > a"));
            
			WebElement el = driver.findElement(By.cssSelector("#table1Body"));

			List<WebElement> tr = el.findElements(By.tagName("tr"));
			// 주소 가져오기, 여기서 이전 신고내용 및 기간관련 필터링이 들어가야 한다.
			for (WebElement row : tr) {
                count-=1;
				WebElement hiddenInputVal = row.findElement(By.cssSelector(String.format("td > input[type=hidden]")));
                
				String state = row.findElement(By.cssSelector("td.bbs_subject > span")).getAttribute("class");
				if(!"ico_state_end".equals(state)) continue;
                // System.out.println(hiddenInputVal.getDomAttribute("value"));
				if(hiddenInputVal !=null) {
					list.add(String.format("window.open('https://www.safetyreport.go.kr/#mypage/mysafereport/%s');",hiddenInputVal.getDomAttribute("value")));
				}
			}
            if(count > 0){
                nextButton.click();
                MyUtils.sleep(500);
            }
		}

        return list;
    }

    @Override
    public String setFileName(WebDriver webdriver) {
        // TODO Auto-generated method stub
        String fileName = "default";
        try {
            fileName = webdriver.findElement(By.cssSelector("#contents > div:nth-child(4) > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong")).getText();
        } catch (Exception e) {
            // TODO: handle exception
            fileName="fail to get title";
        }
        return fileName;
    }
    public static void main(String[] args) throws Exception {
        String[] propertyNames = new String[]{"src/singo2.properties","src/singo.properties"};
        // String[] propertyNames = new String[]{"src/singo.properties"};
        for (String propertyName : propertyNames) {
            getImageSaver(propertyName);
        }
    }

    private static void getImageSaver(String propertyName) throws Exception {
        Map<String,String> propertiesMap = MyUtils.loadProperties(propertyName);
        
        WebDriver driver = MyUtils.getWebDriver();
        driver.get("https://www.safetyreport.go.kr/#main/login/login");
        String mainWindow = driver.getWindowHandle();
        
        LoginInfo loginInfo = new LoginInfo(propertiesMap.get("userid"), propertiesMap.get("password"), null);
        Sinmungo sinmungo = new Sinmungo(driver);

        sinmungo.login(driver, loginInfo);

        driver.get("https://www.safetyreport.go.kr/#/mypage/mysafereport");
        sinmungo.waiter(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#table1Body")));
        List<String> addresses = sinmungo.loading(driver);

        String saveDir = "src/images/test/"+propertiesMap.get("userid")+"/";
		ImageMerge imageSaver = new ImageMerge(driver,saveDir, sinmungo);

        imageSaver.asyncSaveImageYX(mainWindow, webdriver->{
            for (int j = 0; j < 3; j++) {
                int h =driver.findElement(By.xpath("/html/body")).getSize().getHeight();
                if(h>1000){
                    // driver.findElement(By.cssSelector("#contents > div:nth-child(4) > table > tbody > tr:nth-child(7) > td")).getText();
                    return true && driver.findElement(By.cssSelector("#contents > div:nth-child(4) > table > tbody > tr:nth-child(7) > td")).getText().contains("이륜차 위반 메뉴로 접수된 신고");
                } else{
                    MyUtils.sleep(1000);
                }
            }
            return false;
        },addresses,By.xpath("/html/body"));
    }
}
