package com.test.website;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.dto.LoginInfo;
import com.test.interfaces.WebPageLoading;
import com.test.interfaces.WebPageLogin;
import com.test.interfaces.WebpageCrowlable;
import com.test.util.ImageMerge;
import com.test.util.MyUtils;

public class Sinmungo2 extends WebpageCrowlable {

    WebDriver driver;
    JavascriptExecutor js;

    WebDriverWait waiter;
    
    public Sinmungo2(WebDriver webDriver) {
        this.driver = webDriver;
        this.js = (JavascriptExecutor)webDriver;
        this.waiter = new WebDriverWait(driver, Duration.of(3000, ChronoUnit.MILLIS));

    }

    // @Override
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
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<>();
        int count = Integer.parseInt(driver.findElement(By.cssSelector("#contents > div.table_bbs.list.tb_sty01 > p > strong")).getText());        

        int loop = count/10 + count %10>0?1:0;
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
}
