package com.test.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.dto.LoginInfo;
import com.test.website.SmartJebo;

// import io.github.bonigarcia.wdm.WebDriverManager;

public class Capture {
	private WebDriver driver;
	private JavascriptExecutor js;
	private WebDriverWait waiter;

	private WebElement webElement;
	private String base_url;
	private Map<String, String> propertiesMap;

	private String saveDir = "src/images/";

	public Capture(String base_url, boolean show) throws Exception {
		
		super();
//		WebDriverManager.chromedriver().setup();
		System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
		
		// System.out.printf("chrome driver path : %s by webDriverManager \n",WebDriverManager.chromedriver().getDownloadedDriverPath());
		// os 에 따른 크롬드라이버 선택
		String osName = System.getProperty("os.name").toLowerCase();
		System.out.printf("os name is : %s\n", osName);
		
		this.base_url = base_url;
		ChromeOptions options = new ChromeOptions();

		// 도커를 이용해 리눅스 환경에서 실행을 시도하던 부분
		//-> 보류, 화면없이 실행시 크기가 800 x 400으로 나온다. 이거 캡쳐하려면 새로 merge 외에도 가로 merge를 해야하기 때문....
		// 크롬웹앱으로 작업 진행 예정
//		options.addArguments("--headless");	
//		options.addArguments("--no-sandbox");
//		options.addArguments("--disable-gpu");
//		options.addArguments("--disable-dev-shm-usage");

		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability("acceptInsecureCerts", true);
		options.addArguments("--start-maximized");
		
		driver = new ChromeDriver(options);
		waiter = new WebDriverWait(driver, Duration.of(3000, ChronoUnit.MILLIS));
		js = (JavascriptExecutor) driver;
		File savedir = new File(saveDir);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
	}
	public Capture build(String properties) {
		try {
			loadProperties(properties);
			saveDir = convertEncoding(propertiesMap.getOrDefault("saveDir", saveDir).toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	private void loadProperties(String prop) throws Exception {
		Properties properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream(new File(prop))));

		if (propertiesMap == null) {
			propertiesMap = new HashMap<>();
		}

		System.out.println();
		System.out.println("\t\tchecking your properties file\n");
		for (Object ob : properties.keySet()) {
			System.out.println("\t\t" + ob + " : \t "
					+ new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
			propertiesMap.put((String) ob,
					new String(properties.getProperty(ob.toString()).getBytes("ISO-8859-1"), "utf-8"));
		}
	}
	private String convertEncoding(String iso) {
		String a = "";
		try {
			a = new String(iso.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	public void doSinmungo() throws Exception{
		driver.get(base_url);
		sleep(1000);
		Actions action = new Actions(driver);
		Window window = driver.manage().window();
		
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(propertiesMap.get("userid"));
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(propertiesMap.get("password"));
		driver.findElement(By.xpath("//*[@id=\"contents\"]/div/ul/li[1]/article/div[1]/p[3]/button")).click();
		sleep(500);
		
		driver.get("https://www.safetyreport.go.kr/#/mypage/mysafereport");

		try {
			waiter.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#table1Body")));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			System.out.println("대기 실패");
		}

		List<String> list = new ArrayList<>();
		
		sleep(1000);
		// 전체 갯수
		int count = Integer.parseInt(driver.findElement(By.cssSelector("#contents > div.table_bbs.list.tb_sty01 > p > strong")).getText());

		// 페이지 확인
		List<WebElement> pages = driver.findElements(By.cssSelector("#table1 > tfoot > tr > td > ul > li.footable-page.visible > a"));
		for (WebElement page : pages) {
			page.click(); // 페이지 이동
			sleep(1000);
			WebElement el = driver.findElement(By.cssSelector("#table1Body"));

			List<WebElement> tr = el.findElements(By.tagName("tr"));
			// 주소 캐오기, 여기서 이전 신고내용 및 기간관련 필터링이 들어가야 한다.
			for (WebElement row : tr) {
				WebElement hiddenInputVal = row.findElement(By.cssSelector(String.format("td > input[type=hidden]")));
				String date = row.findElement(By.cssSelector(String.format("td:nth-child(2)"))).getText();
				Date dt = MyUtils.toDate(new SimpleDateFormat("yyyy-mm-dd"),date );
				String state = row.findElement(By.cssSelector("td.bbs_subject > span")).getAttribute("class");
				if(!"ico_state_end".equals(state)) continue;
				if(hiddenInputVal !=null) {
					list.add(hiddenInputVal.getDomAttribute("value"));
				}
			}
		}

		String mainWindow = driver.getWindowHandle();
		ImageMerge im = new ImageMerge(driver,"src/images/test/", webdriver -> {
			String namaewa = "default";
			try {
				namaewa = webdriver.findElement(By.cssSelector("#contents > div:nth-child(4) > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong")).getText();
			} catch (Exception e) {
				// TODO: handle exception
				namaewa="fail to get title";
			}
			return namaewa;
		});
		File[] images = null;
		String fileName = "";
		
		// 새창에서 열기
		for(int i =0; i<list.size();i++) {
			String address = list.get(i);
			String temp = String.format("window.open('https://www.safetyreport.go.kr/#mypage/mysafereport/%s');", address);
			js.executeScript(temp);
			// 탭의 개수가 10가 추가되면 저장 시작
			if ((driver.getWindowHandles().size() - 1) % 10 == 0 || i == list.size()-1) { 
				for (String w : driver.getWindowHandles()) {
					if (w.equals(mainWindow))
						continue; 
					images = im.imageSave(mainWindow, w);
					fileName = im.getFileName();
					im.mergeImage(images,null);
				}
				driver.switchTo().window(mainWindow);
				sleep(100);
			}
		}
		System.out.println();
	}

	public void doSmartReport() {
		driver.get(base_url);
		sleep(1000);
		Actions action = new Actions(driver);
		Window window = driver.manage().window();
		
		SmartJebo smartJebo = new SmartJebo(driver, new LoginInfo(propertiesMap.get("id"), propertiesMap.get("password"), null));
		System.out.println(window.getSize().getWidth() + " : " + window.getSize().getHeight());
		try {
			// 혹시 alert가 안 뜨는 경우도 있으니까
			waiter.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().dismiss();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// 가상키패드 체크하기 
		js.executeScript("document.getElementById('checkpwd').setAttribute('checked',true)");
		sleep(500);
		Map<String, String> keyMap = new HashMap<>();
		// 저장된 키패드 목록이 있으면 불러온다.
		try {
			smartJebo.loadKeypad(keyMap);
		} catch (Exception e) {
			// 저장된 키보드가 없으면 키보드를 읽어온다.
			smartJebo.keypadExtract(keyMap);
		}

		// 키패드 저장하기
		smartJebo.saveKeypad(keyMap);

		// 로그인 부분
		String pw = propertiesMap.get("password");
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

		driver.findElement(By.xpath("//*[@id=\"j_username\"]")).sendKeys(propertiesMap.get("userid"));
		driver.findElement(By.xpath("//*[@id=\"btn_login\"]")).click();

		sleep(500);
		// 로그인 완료
		// 리스트 돌면서 캡쳐 준비
		driver.get("http://onetouch.police.go.kr/mypage/myGiveInfoList.do");
		sleep(200);
		System.out.println("진짜");
		System.out.println(driver.getCurrentUrl());

		if(true){
			return;
		}
		// 처리일을 입력받아서 진행 
		js.executeScript(String.format("$(\"#mFromDate\").val('%s')",propertiesMap.get("from"))); // 기간검색 시작일
		js.executeScript(String.format("$(\"#mToDate\").val('%s')",propertiesMap.get("to"))); // 기간검색 종료일
		js.executeScript("$(\"#procDt\").prop('checked',true)"); // 처리일 기준으로 선택
		js.executeScript("$(\"#mGvnfProcessSttus > option:nth-child(4)\").prop('selected',true)"); // 답변 완료만 검색
		js.executeScript("fnSearch()");
		sleep(200);
		waiter.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.xpath("//*[@id=\"container\"]/div[2]"), By.xpath("//*[@id=\"container\"]/div[2]/div[3]")));
		String mainWindow = driver.getWindowHandle();
		// 팝업 치우기s
		for (String win : driver.getWindowHandles()) {
			if (!mainWindow.equals(win)) {
				driver.switchTo().window(win).close();
			}
		}
		driver.switchTo().window(mainWindow);
		
		// 검색되는 개수 체크
		int count = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/div[2]/p[1]/span")).getText());
		int page = 1;

		System.out.println("total count : " + count);
		// imageMerge 저장, 저장할 이름 설정
		ImageMerge im = new ImageMerge(driver,"src/images/test/", webdriver -> {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(webdriver.findElement(By.cssSelector("#container > div.content > div:nth-child(4) > table > tbody > tr:nth-child(2) > td:nth-child(4)")).getText().replaceAll(":", "-"));
			stringBuilder.append(".");
			stringBuilder.append(webdriver.findElement(By.cssSelector("#container > div.content > div:nth-child(4) > table > tbody > tr:nth-child(4) > td:nth-child(2)")).getText());
			return stringBuilder.toString();
		});
		File[] images = null;
		String fileName = "";
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
				js.executeScript("window.open('http://onetouch.police.go.kr/mypage/myGiveInfoView.do?gvnfSrcSe="+gvnfSrcSe+"&gvnfSn="+ gvnfSn + "&title=" + imageIndex + "');");
				
//			driver.switchTo().window(mainWindow);
				// 10개씩 처리하기
//			if ((i + 1) % 10 == 0) {
				if ((driver.getWindowHandles().size() - 1) % 10 == 0) { // 탭의 개수가 10가 추가되면 저장 시작
					for (String w : driver.getWindowHandles()) {
						if (w.equals(mainWindow))
							continue; 
						images = im.imageSave(mainWindow, w);
						fileName = im.getFileName();
						im.mergeImage(images,null);
					}
					driver.switchTo().window(mainWindow);
					sleep(100);
				}
//				return;
			}
			if (Nth%10==0) {
				js.executeScript(String.format("linkPage(%d)", page += 1));
				continue;
			}

		}
		for (String w : driver.getWindowHandles()) {
			if (w.equals(mainWindow)) continue;

			images = im.imageSave(mainWindow, w);
			fileName = im.getFileName();
			im.mergeImage(images,null);
		}
		driver.switchTo().window(mainWindow);
		sleep(100);

	}

	public void imageSaveTest() {
		driver.get("chrome://version/");
		try {
			Thread.sleep(1000);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		String main = driver.getWindowHandle();
		js.executeScript("window.open('https://ko.wikipedia.org/wiki/%EC%9C%84%ED%82%A4%EB%B0%B1%EA%B3%BC:%EB%8C%80%EB%AC%B8');");
		for (String window: driver.getWindowHandles()) {
			if(window.equals(main)) continue;
			imageSave(main, window);			
		}
	}

	@Deprecated
	private void imageSave(String mainWindow, String windowName) {
		driver.switchTo().window(windowName);
		webElement = driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]"));
		int windowHeight = driver.manage().window().getSize().height;
		int contentHeight = webElement.getSize().getHeight();

		int n = (int) contentHeight / (windowHeight / 2)+ 1;
		System.out.println(windowHeight);
		File[] scrFile = new File[n];
		for (int k = 0; k < n; k++) {
			scrFile[k] = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			js.executeScript("window.scrollTo(0," + (windowHeight / 2) * (k + 1) + ")");
			sleep(10);
		}
		mergeImage(
				scrFile,
//				driver.getCurrentUrl().split("title")[1].substring(1) + "."+ driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/div[1]/table/tbody/tr[4]/td[1]")).getText(), // 임시부여한 번호
//				driver.findElement(By.cssSelector("#container > div.content > div:nth-child(4) > table > tbody > tr:nth-child(2) > td:nth-child(4)")).getText().replaceAll(":", "-") // 위반일시
				 "."
//				 driver.findElement(By.cssSelector("#container > div.content > div:nth-child(4) > table > tbody > tr:nth-child(4) > td:nth-child(2)")).getText(), // 위반차량번호
				+"myTest",
				windowHeight);
		driver.close();
	}
	@Deprecated
	private void mergeImage(File[] images, String fileName, int windowHeight) {
		try {
			BufferedImage[] is = new BufferedImage[images.length];

			int width = 0;
			int height = 0;
			int minus = 0;
			for (int i = 0; i < is.length; i++) {
				is[i] = ImageIO.read(images[i]);
				width = Math.max(width, is[i].getWidth());
				height += is[i].getHeight();
				minus = is[i].getHeight();
			}
			// 파일 높이 조정
			height -= ((minus / 2 - windowHeight/14) * (is.length - 1));

			BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			System.out.println(windowHeight);
			for (int i = 0; i < is.length; i++) {
				graphics.drawImage(is[i], 0, tempHeight - (i * ((windowHeight/2) - (windowHeight/12))), null);
				tempHeight += is[i].getHeight();
			}
			Arrays.stream(images).forEach(t -> t.delete());
			ImageIO.write(mergedImage, "png", new File(saveDir+ fileName + ".png"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Deprecated
	private void copy(File origin, File to) throws Exception {
		FileInputStream input = new FileInputStream(origin);

		FileOutputStream oss = new FileOutputStream(to);

		// 3. 한번에 read하고, write할 사이즈 지정
		byte[] buf = new byte[1024];

		// 4. buf 사이즈만큼 input에서 데이터를 읽어서, output에 쓴다.
		int readData;

		while ((readData = input.read(buf)) > 0) {
			oss.write(buf, 0, readData);
		}

		// 5. Stream close
		input.close();
		oss.close();
	}

	private void sleep(long mil) {
		try {
			Thread.sleep(mil);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 파일 덮어쓰고 덮은 파일 지우기 파일마다 시작과 끝 부분이 있는 듯 하다. 첫번째 입력된 파일만 읽어낸다. 첫번째 파일의 크기를 넘기면
	 * 다음 파일이 드러난다.
	 * 
	 * @param origin
	 * @param to
	 * @param remove
	 */
	@Deprecated
	public void fileChanger(File origin, File to, long remove) {
		try {
			FileInputStream input = new FileInputStream(origin);
			FileOutputStream oss = new FileOutputStream(to);

			System.out.println("origin length " + origin.length());
			System.out.println("input length " + input.getChannel().size());
			byte[] buf = new byte[1024];

			// 4. buf 사이즈만큼 input에서 데이터를 읽어서, output에 쓴다.
			int readData;
			for (int j = 0; j < remove; j++) {
				input.read();
				if (j > remove - 30) {
					System.out.print((char) input.read());
				}
			}
			System.out.println("-----------------");
			while ((readData = input.read(buf)) > 0) {
				oss.write(buf, 0, readData);
//				if(readData < 1024) {
//					System.out.println(new String(buf,"ISO-8859-1"));
//					System.out.println(new String(buf,"utf-8"));
//					System.out.println(new String(buf,"ms949"));
//				}
			}

			// 5. Stream close
			input.close();
			oss.close();

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
