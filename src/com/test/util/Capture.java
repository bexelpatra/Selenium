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
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
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

public class Capture {
	private WebDriver driver;
	private JavascriptExecutor js;
	private WebDriverWait waiter;

	private WebElement webElement;

	private final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	private String WEB_DRIVER_PATH = "src/chromedriver.exe";
	private String base_url;
	private Map<String, String> propertiesMap;

	private String saveDir = "src/images/";

	public Capture(String base_url, boolean show) throws Exception {

		super();

		// os 에 따른 크롬드라이버 선택
		String osName = System.getProperty("os.name").toLowerCase();
//		if (osName != null && osName.contains("windows")) {
//			WEB_DRIVER_PATH += ".exe";
//		} else if (osName != null && osName.contains("linux")) {
//			WEB_DRIVER_PATH += "Linux";
//		}
		System.out.printf("os name is : %s\n", osName);
		System.out.println(WEB_DRIVER_PATH);

		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
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
		loadProperties();
		File savedir = new File(saveDir);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
	}

	private void loadProperties() throws Exception {
		Properties properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream(new File("src/singo2.properties"))));

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
		saveDir = convertEncoding(properties.getOrDefault("saveDir", saveDir).toString());

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

	public void doJob() {
		driver.get(base_url);
		sleep(1000);
		Actions action = new Actions(driver);
		Window window = driver.manage().window();
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
			loadKeypad(keyMap);
		} catch (Exception e) {
			// 저장된 키보드가 없으면 키보드를 읽어온다.
			keypadExtract(keyMap);
		}

		// 키패드 저장하기
		saveKeypad(keyMap);

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
		int count = Integer
				.parseInt(driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/div[2]/p[1]/span")).getText());
		int page = 1;

		// if (count < 20) {
//			System.out.println("total count is to low, you can use this when the count is equal or lager than 20");
//			return;
//		}
		System.out.println("total count : " + count);
		for (int i = 0; i < count; i++) {
			int Nth = (i) % 10 + 1; // 화면 속 순서
			// 처리상태
			webElement = driver.findElement(
					By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth
							+ ") > td:nth-child(5)"));
			String chargeResult = webElement.getText();

			// 이륜차 신고 이외의 건은 거른다.
			webElement = driver.findElement(
					By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth
							+ ") > td:nth-child(3)"));
			String checkBiCycle =webElement.getText();

			// 답변 완료 아니면 스킵
			if (chargeResult != null && chargeResult.startsWith("답변완료") && checkBiCycle.contains("이륜차")) {				
				// 클릭 이벤트 관련 a태그
				webElement = driver.findElement(
						By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth
								+ ") > td:nth-child(2) > a"));
				// 페이지 이동을 위해 parameter 추출
				String tempstr = webElement.getAttribute("href").split(":")[1];
				int a = tempstr.indexOf('\'');
				int b = tempstr.indexOf('\'', a + 1);
				String gvnfSn= tempstr.substring(a + 1, b);
				int c = tempstr.indexOf('\'', b + 1);
				int d = tempstr.indexOf('\'', c + 1);
				String gvnfSrcSe= tempstr.substring(c + 1, d);
				
				webElement = driver.findElement(
						By.cssSelector("#container > div.content > div.table_list > table > tbody > tr:nth-child(" + Nth
								+ ") > td:nth-child(1)"));
				String imageIndex = webElement.getText();
				// 새창에서 열기
				js.executeScript(
						"window.open('http://onetouch.police.go.kr/mypage/myGiveInfoView.do?gvnfSrcSe="+gvnfSrcSe+"&gvnfSn="
								+ gvnfSn + "&title=" + imageIndex + "');");
				
//			driver.switchTo().window(mainWindow);
				// 10개씩 처리하기
//			if ((i + 1) % 10 == 0) {
				if ((driver.getWindowHandles().size() - 1) % 10 == 0) {
					for (String w : driver.getWindowHandles()) {
						if (w.equals(mainWindow))
							continue;
						imageSave(mainWindow, w);
					}
					driver.switchTo().window(mainWindow);
					sleep(100);
				}
			}
			if (Nth%10==0) {
				js.executeScript(String.format("linkPage(%d)", page += 1));
				continue;
			}

		}
		for (String w : driver.getWindowHandles()) {
			if (w.equals(mainWindow))
				continue;
			imageSave(mainWindow, w);
		}
		driver.switchTo().window(mainWindow);
		sleep(100);

	}

	private void loadKeypad(Map<String, String> keyMap) throws FileNotFoundException, IOException, ParseException {
		BufferedReader reader = new BufferedReader(new FileReader(new File("src/keypad.txt")));
		String jsonKeypad = reader.readLine();
		JSONObject keyJO = (JSONObject) new JSONParser().parse(jsonKeypad);
		for (Object key : keyJO.keySet()) {
			keyMap.put(key.toString(), keyJO.get(key).toString());
		}
	}

	private void keypadExtract(Map<String, String> keyMap) {
		String handle = "";
		String key = "";
		String val = "";

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

	private void saveKeypad(Map<String, String> keyMap) {
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

	private void imageSave(String mainWindow, String windowName) {
		driver.switchTo().window(windowName);
		webElement = driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]"));
		int height = webElement.getSize().getHeight();

		int n = (int) height / 400 + 1;
		File[] scrFile = new File[n];
		for (int k = 0; k < n; k++) {
			scrFile[k] = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			js.executeScript("window.scrollTo(0," + 400 * (k + 1) + ")");
			sleep(10);
		}
		mergeImage(scrFile, driver.getCurrentUrl().split("title")[1].substring(1) + "."+ driver
				.findElement(By.xpath("//*[@id=\"container\"]/div[2]/div[1]/table/tbody/tr[4]/td[1]")).getText(),
				height);
		driver.close();
	}

	private void mergeImage(File[] images, String fileName, int h) {
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
			height -= ((minus / 2 - 30) * (is.length - 1));

			BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				graphics.drawImage(is[i], 0, tempHeight - (i * (400 - 20)), null);
				tempHeight += is[i].getHeight();
			}
			Arrays.stream(images).forEach(t -> t.delete());
			ImageIO.write(mergedImage, "png", new File(saveDir+ fileName + ".png"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

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

	@Deprecated
	public void fileConnet(File[] origin, File to) throws Exception {
		FileInputStream[] input = new FileInputStream[origin.length];

		FileOutputStream oss = new FileOutputStream(to);

		System.out.println("file size ");
		for (int i = 0; i < origin.length; i++) {
			input[i] = new FileInputStream(origin[i]);
			System.out.println(input[i].getChannel().size() + ", " + origin[i].length());

		}
		// 3. 한번에 read하고, write할 사이즈 지정
		byte[] buf = new byte[1024];

		// 4. buf 사이즈만큼 input에서 데이터를 읽어서, output에 쓴다.
		int readData = 0;
		int bytes = 0;
		for (int i = 0; i < input.length; i++) {
			while ((readData = input[i].read(buf)) > 0) {
				oss.write(buf, 0, readData);
			}
			input[i].close();
		}
		// 5. Stream close
		oss.close();
		fileChanger(to, new File("c:/Temp/merge.png"), origin[0].length());
	}

	@Deprecated
	private void setWindowSize(int width, int height) {
		driver.manage().window().setSize(new Dimension(width, height));
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
