package com.test.website;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.test.dto.LoginInfo;
import com.test.interfaces.WebPageLoading;
import com.test.interfaces.WebPageLogin;

public class SmartJebo implements WebPageLogin, WebPageLoading{

    WebDriver driver;
    JavascriptExecutor js;

    LoginInfo loginInfo;
    
    public SmartJebo(WebDriver webDriver,LoginInfo loginInfo) {
        this.driver = webDriver;
        this.js = (JavascriptExecutor)webDriver;
        this.loginInfo = loginInfo;
    }

    public void loadKeypad(Map<String, String> keyMap) throws FileNotFoundException, IOException, ParseException {
		BufferedReader reader = new BufferedReader(new FileReader(new File("src/keypad.txt")));
		String jsonKeypad = reader.readLine();
		JSONObject keyJO = (JSONObject) new JSONParser().parse(jsonKeypad);
		for (Object key : keyJO.keySet()) {
			keyMap.put(key.toString(), keyJO.get(key).toString());
		}
	}

    public void keypadExtract(Map<String, String> keyMap) {
		String handle = "";
		String key = "";
		String val = "";
        WebElement webElement;
		for (int i = 0; i < 3; i++) {
			if (i == 0) {

			} else if (i == 1) {
				handle = "VPad.ClickHangul();";
			} else {
				handle = "VPad.ClickShift();";
			}
			js.executeScript(handle);
			for (int j = 0; j < 47; j++) {
				webElement = driver.findElement(By.id("m_charText" + j + ""));
				key = webElement.getText();
				if (keyMap.get(key) == null) {
					val = handle + webElement.getAttribute("onclick") + ";" + handle;
					keyMap.put(key, val);
				}
			}
			js.executeScript(handle);
		}
	}

	public void saveKeypad(Map<String, String> keyMap) {
		String keypadjson = new JSONObject().toJSONString(keyMap);
		try {
			FileOutputStream os = new FileOutputStream(new File("src/keypad.txt"));
			os.write(keypadjson.getBytes());
			os.close();
			System.out.println("키패드 저장 완료");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    public void login(WebDriver driver, LoginInfo loginInfo) {
        // TODO Auto-generated method stub
        js.executeScript("document.getElementById('checkpwd').setAttribute('checked',true)");
		sleep(500);
		Map<String, String> keyMap = new HashMap<>();
		// 저장된 키패드 목록이 있으면 불러온다.
		try {
			loadKeypad(keyMap);
		} catch (Exception e) {
			// 저장된 키보드가 없으면 키보드를 읽어온다.
			keypadExtract(keyMap);
		}
		// 키패드 저장하기
		saveKeypad(keyMap);
        String id =loginInfo.getId();
		String pw = loginInfo.getPw();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pw.length(); i++) {
			String temp = keyMap.get(String.valueOf(pw.charAt(i)));
			sb.append(temp);
			sb.append("\n");
		}
		
        sb.append("VPad.ClickEnter('char');");
		pw = sb.toString();
		js.executeScript(pw);
		sleep(500);

		driver.findElement(By.xpath("//*[@id=\"j_username\"]")).sendKeys(id);
		driver.findElement(By.xpath("//*[@id=\"btn_login\"]")).click();

		sleep(500);        
    }

    @Override
    public List<String> loading(WebDriver driver) {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<>();
        int count = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/div[2]/p[1]/span")).getText());
		int page = 1;

        WebElement webElement;

        for (int i = 0; i < count; i++) {
			int Nth = (i) % 10 + 1; // 화면 속 순서
			// 처리상태
			webElement = driver.findElement(By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" +Nth+ ") > td:nth-child(5)"));
			String chargeResult = webElement.getText();

			// 이륜차 신고 이외의 건은 거른다.
			webElement = driver.findElement(By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth + ") > td:nth-child(3)"));
			String checkBiCycle =webElement.getText();

			// 답변 완료 아니면 스킵
			if (chargeResult != null && chargeResult.startsWith("답변완료") && checkBiCycle.contains("이륜차")) {				
				// 클릭 이벤트 관련 a태그
				webElement = driver.findElement(By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth + ") > td:nth-child(2) > a"));
				// 페이지 이동을 위해 parameter 추출
				String tempstr = webElement.getAttribute("href").split(":")[1];
				int a = tempstr.indexOf('\'');
				int b = tempstr.indexOf('\'', a + 1);
				String gvnfSn= tempstr.substring(a + 1, b);
				int c = tempstr.indexOf('\'', b + 1);
				int d = tempstr.indexOf('\'', c + 1);
				String gvnfSrcSe= tempstr.substring(c + 1, d);
				
				webElement = driver.findElement(By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth + ") > td:nth-child(1)"));
				String imageIndex = webElement.getText();
				// 새창에서 열기
				// js.executeScript("window.open('http://onetouch.police.go.kr/mypage/myGiveInfoView.do?gvnfSrcSe="+gvnfSrcSe+"&gvnfSn="+ gvnfSn + "&title=" + imageIndex + "');");
                list.add("http://onetouch.police.go.kr/mypage/myGiveInfoView.do?gvnfSrcSe="+gvnfSrcSe+"&gvnfSn="+ gvnfSn + "&title=" + imageIndex);
				
			}
			if (Nth%10==0) {
				js.executeScript(String.format("linkPage(%d)", page += 1));
				continue;
			}

		}
        return list;
    }

    private void sleep(long mil){
        try {
            Thread.sleep(mil);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
