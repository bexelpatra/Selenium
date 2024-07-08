package com.test.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.dto.MergeInfo;
import com.test.interfaces.FilenameSetter;
import com.test.interfaces.Waiter;

public class ImageMerge {
	private WebDriver driver;
	private WebDriverWait driverWait;
	private JavascriptExecutor js;

	private WebElement webElement;
	private FilenameSetter filenameSetter;

	private String saveDir = "";
	private String fileName = "";
	
	private int scrollDown =0;
	// private int elementHeight = 0;
	// private int lastScrollHeigh = 0;
	private int tabNumbers = 5;

	private ConcurrentHashMap<String,MergeInfo> concurrentHashMap = new ConcurrentHashMap<>();
	public ImageMerge(WebDriver driver,String saveDir,FilenameSetter filenameSetter) {
		super();
		this.driver = driver;
		this.driverWait = new WebDriverWait(driver, Duration.of(3000, ChronoUnit.MILLIS));
		this.filenameSetter = filenameSetter;
		this.js = (JavascriptExecutor) driver;
		this.scrollDown=driver.manage().window().getSize().getHeight()/2;

		if(saveDir!=null){
			this.saveDir = saveDir;
			File dir = new File(saveDir);
			if(!dir.exists()){
				if(!dir.mkdirs()){
					this.saveDir = "src/images/test/";
				};
			}
		}
	}
	public File[] imageSave(String mainWindow, String windowName,By by) {
		driver.switchTo().window(windowName);
		// webElement = driver.findElement(By.xpath("/html/body"));
		webElement = driver.findElement(by);
		int windowHeight = driver.manage().window().getSize().height;
		int windowWidth = driver.manage().window().getSize().width;
		int contentHeight = webElement.getSize().getHeight();
		int contentWidth = webElement.getSize().getWidth();
		String fileName = filenameSetter.setFileName(driver);

		System.out.printf("filename : %s  window height : %d , window width : %d, content height : %d  content width : %d\n",fileName,windowHeight,windowWidth, contentHeight, contentWidth);
		File[] srcFile = new File[contentHeight/(windowHeight/2)];
		Object ob = null;
		int totalCapturedImg = 0;
		int imgHeight = 0;
		int imgWitdh = 0;
		int k=0;
		Object lastY = null;
		Object lastX = null;

		BufferedImage temp=MyUtils.getBufferedImage(webElement.getScreenshotAs(OutputType.FILE));
		imgHeight = temp.getHeight();
		imgWitdh = temp.getWidth();
		System.out.println(srcFile.length+" : "+imgHeight + " : " + imgWitdh);
		
		while (totalCapturedImg <= contentHeight+100) {
			srcFile[k] = ((TakesScreenshot) webElement).getScreenshotAs(OutputType.FILE);
			js.executeScript("window.scrollTo(0," + (imgHeight)* (k + 1) + ")");
			k+=1;
			totalCapturedImg +=imgHeight;
			ob = js.executeScript("return window.scrollY");
			if(lastY!= null && lastY.equals(ob)) {
				break;
			}
			lastY= ob;
			// System.out.println(ob);
			sleep(10);
		}
		int lastScrollHeigh = Math.round(Float.parseFloat(String.valueOf(lastY)));



		concurrentHashMap.put(windowName, new MergeInfo(fileName, contentHeight, contentWidth, lastScrollHeigh, lastScrollHeigh));
		driver.close();
		
		return srcFile;
	}
	
	// y,x 축 양쪽을 계산한다.
	public File[][] imageSave2D(String mainWindow, String windowName,By by) {
		driver.switchTo().window(windowName);
		webElement = driver.findElement(by);
		int windowHeight = driver.manage().window().getSize().height;
		int windowWidth = driver.manage().window().getSize().width;
		int contentHeight = webElement.getSize().getHeight();
		int contentWidth = webElement.getSize().getWidth();
		String fileName = filenameSetter.setFileName(driver);

		System.out.printf("filename : %s  window height : %d , window width : %d, content height : %d  content width : %d\n",fileName,windowHeight,windowWidth, contentHeight, contentWidth);
		File[][] srcFile = new File[contentHeight/(windowHeight/2)][contentWidth/(windowWidth/2) ];

		Object ob = null;
		
		int totalCapturedHeight = 0;
		int totalCapturedWidth = 0;

		int imgHeight = 0;
		int imgWitdh = 0;
		
		int y=0;
		int x=0;
		Object lastY = null;
		Object lastX = null;

		int lastScrollHeigh = 0;
		int lastScrollWidth = 0;
		
		BufferedImage temp=MyUtils.getBufferedImage(webElement.getScreenshotAs(OutputType.FILE));

		imgHeight = temp.getHeight();
		imgWitdh = temp.getWidth();
		// 마우스로 한클릭이 40임
		
		// y축 먼저 저장하고 x축 옳긴 다음 반복
		while(totalCapturedWidth <= contentWidth + 80){
			while (totalCapturedHeight <= contentHeight+80) {
				srcFile[y][x] = ((TakesScreenshot) webElement).getScreenshotAs(OutputType.FILE);

				js.executeScript(String.format("window.scrollTo(%d,%d)", (imgWitdh)*(x),(imgHeight)* (y + 1)));
				y+=1;
				totalCapturedHeight +=imgHeight;
				ob = js.executeScript("return window.scrollY");
				if(lastY!= null && lastY.equals(ob)) {
					break;
				}
				lastY= ob;
				// System.out.println(ob);
				sleep(10);
			}

			totalCapturedHeight = 0; // 높이 초기화 + x축 이동
			y=0;

			js.executeScript(String.format("window.scrollTo(%d,%d)", (imgWitdh)*(x+1),0));
			x+=1;
			totalCapturedWidth += imgWitdh;
			ob = js.executeScript("return window.scrollX");
			
			if(lastX!=null &&lastX.equals(ob)){
				break;
			}
			lastX = ob;
			if(Integer.parseInt(lastX.toString() )<= 80){
				break;
			}
		}
		lastScrollHeigh = Math.round(Float.parseFloat(String.valueOf(lastY)));
		lastScrollWidth = Math.round(Float.parseFloat(String.valueOf(lastX)));
		// y축 저장하기
		
		concurrentHashMap.put(windowName, new MergeInfo(fileName, contentHeight, contentWidth, lastScrollHeigh, lastScrollWidth));
		driver.close();
		return srcFile;
	}
	public void mergeImage(File[][] images, MergeInfo mergeInfo) {

		int imagesCountY = (int)Arrays.stream(images).filter(t -> t[0]!=null).count();
		int imagesCountX = (int)Arrays.stream(images[0]).filter(t -> t!=null).count();
		String fileName = mergeInfo.getFileName();
		int contentHeight = mergeInfo.getContentHeight();
		int contentWidth = mergeInfo.getContentWidth();
		
		int lastScrollHeigh = mergeInfo.getLastScrollHeigh();
		int lastScrollWidth = mergeInfo.getLastScrollWidth();

		try {
			BufferedImage[][] is = new BufferedImage[imagesCountY][imagesCountX];

			FileInputStream[][] fisArr = new FileInputStream[imagesCountY][imagesCountX];
			for (int i = 0; i < imagesCountY; i++) {
				if(images[i]==null) continue;

				for (int j = 0; j < imagesCountX; j++) {
					if(images[i][j]==null) continue;
					
					fisArr[i][j] = new FileInputStream(images[i][j]);
					is[i][j] = ImageIO.read(fisArr[i][j]);
				}
			}
			BufferedImage mergedImage = new BufferedImage(contentWidth, contentHeight, BufferedImage.TYPE_INT_RGB);
			
			
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);

			int imageHeight = is[0][0].getHeight();
			int imageWidth = is[0][0].getWidth();
			int cutHeight = 0;
			int cutWitdh = 0;
			for (int y = 0; y < imagesCountY; y++) {				
				for (int x = 0; x < imagesCountX; x++) {
					if(is[y][x] == null) continue;
					if(imagesCountY-1 == y ) {
						cutHeight = lastScrollHeigh;
					}
					if(imagesCountX-1 == x){
						cutWitdh = lastScrollWidth;
					}
					graphics.drawImage(is[y][x], cutWitdh, cutHeight, null);
					cutWitdh +=imageWidth;
				}
				cutHeight += imageHeight;
				cutWitdh = 0;
			}

			File saveFile = new File(saveDir+ fileName + ".png");
			int i =0 ;
			while(saveFile.exists()) {
				saveFile = new File(saveDir+ fileName + "_("+i +").png");
				i+=1;
			}
			ImageIO.write(mergedImage, "png",saveFile);
			
			for (int j = 0; j < fisArr.length; j++) {
				for (int j2 = 0; j2 < fisArr[j].length; j2++) {
					if(fisArr[j][j2]!=null){
						fisArr[j][j2].close();
					}
				}
			}
			mergedImage.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void mergeImage(File[] images, MergeInfo mergeInfo) {
		int imagesCount = (int)Arrays.stream(images).filter(t -> t!=null).count();
		String fileName = mergeInfo.getFileName();
		int contentHeight = mergeInfo.getContentHeight();
		int lastScrollHeigh = mergeInfo.getLastScrollHeigh();

		try {
			BufferedImage[] is = new BufferedImage[imagesCount];

			int width = 0;
			FileInputStream[] fisArr = new FileInputStream[is.length];
			for (int i = 0; i < is.length; i++) {
				if(images[i]==null) {
					continue;
				}
				fisArr[i] = new FileInputStream(images[i]);
				is[i] = ImageIO.read(fisArr[i]);
				// is[i] = ImageIO.read(images[i]);
				width = Math.max(width, is[i].getWidth());
			}
			BufferedImage mergedImage = new BufferedImage(width, contentHeight, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				int cutHeight = tempHeight;
				if(is.length-1 == i ) {
					cutHeight = lastScrollHeigh;
				}
				graphics.drawImage(is[i], 0, cutHeight, null);
				tempHeight += is[i].getHeight();
			}

			File saveFile = new File(saveDir+ fileName + ".png");
			int i =0 ;
			while(saveFile.exists()) {
				saveFile = new File(saveDir+ fileName + "_("+i +").png");
				i+=1;
			}
			ImageIO.write(mergedImage, "png",saveFile);

			for (FileInputStream fileInputStream : fisArr) {
				fileInputStream.close();
			}
			for (BufferedImage bufferedImage : is) {
				bufferedImage.flush();
			}
			mergedImage.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean mergeImage(File[] images, MergeInfo mergeInfo,int count) {
		boolean result = false;
		if(count >=3){
			return result;
		}
		int imagesCount = (int)Arrays.stream(images).filter(t -> t!=null).count();
		String fileName = mergeInfo.getFileName();
		int contentHeight = mergeInfo.getContentHeight();
		int lastScrollHeigh = mergeInfo.getLastScrollHeigh();

		BufferedImage[] is = new BufferedImage[imagesCount];

		int width = 0;
		FileInputStream[] fisArr = new FileInputStream[is.length];
		try {
			for (int i = 0; i < is.length; i++) {
				if(images[i]==null) {
					continue;
				}
				fisArr[i] = new FileInputStream(images[i]); // for resource close.
				is[i] = ImageIO.read(fisArr[i]);
				
				// is[i] = ImageIO.read(images[i]);
				width = Math.max(width, is[i].getWidth());
			}
			BufferedImage mergedImage = new BufferedImage(width, contentHeight, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				int cutHeight = tempHeight;
				if(is.length-1 == i ) {
					cutHeight = lastScrollHeigh;
				}
				graphics.drawImage(is[i], 0, cutHeight, null);
				tempHeight += is[i].getHeight();
			}
			File saveFile = new File(saveDir+ fileName + ".png");
			int i =0 ;
			while(saveFile.exists()) {
				saveFile = new File(saveDir+ fileName + "_("+i +").png");
				i+=1;
			}
			ImageIO.write(mergedImage, "png",saveFile);
			graphics.dispose();
			
			for (FileInputStream fileInputStream : fisArr) {
				fileInputStream.close();
			}
			for (BufferedImage bufferedImage : is) {
				bufferedImage.flush();
			}
			mergedImage.flush();
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("save failure retry " + count);
			result = mergeImage(images, mergeInfo, count+1);
		}
		return result;
	}
	private void sleep(long mil) {
		try {
			Thread.sleep(mil);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void saveImage(String mainWindow,List<String> list,By by){
        File[] images = null;
		driver.switchTo().window(mainWindow);

		// 새창에서 열기
		for(int i =0; i<list.size();i++) {
			String address = list.get(i);
			js.executeScript(address);
			if ((driver.getWindowHandles().size() - 1) % tabNumbers == 0 || i == list.size()-1) { 
				for (String w : driver.getWindowHandles()) {
					if (w.equals(mainWindow))
						continue; 
					driver.switchTo().window(w);
					images = imageSave(mainWindow, w,by);
					MergeInfo mergeInfo = concurrentHashMap.get(w);
					mergeImage(images, mergeInfo);
					concurrentHashMap.remove(w);
					MyUtils.deleteFiles(images);
				}
				driver.switchTo().window(mainWindow);
			}
		}
    }
	// 동기화되어서 oos가 자주 나오지는 않는다.
	public void saveImage(String mainWindow,Waiter waiter,List<String> list,By by){
        File[] images = null;
		driver.switchTo().window(mainWindow);

		// 새창에서 열기
		for(int i =0; i<list.size();i++) {
			String address = list.get(i);
			js.executeScript(address);
			if ((driver.getWindowHandles().size() - 1) % tabNumbers == 0 || i == list.size()-1) { 
				for (String w : driver.getWindowHandles()) {
					if (w.equals(mainWindow))
						continue; 
					driver.switchTo().window(w);
					if(waiter.until(driver)){
						images = imageSave(mainWindow, w,by);
						MergeInfo mergeInfo = concurrentHashMap.get(w);
						mergeImage(images, mergeInfo);
						concurrentHashMap.remove(w);
						MyUtils.deleteFiles(images);
					}
				}
				driver.switchTo().window(mainWindow);
				MyUtils.sleep(100);
			}
		}
    }


	public void asyncSaveImage(String mainWindow,Waiter waiter,List<String> list,By by){
		driver.switchTo().window(mainWindow);

		// 새창에서 열기
		for(int i =0; i<list.size();i++) {
			String address = list.get(i);
			js.executeScript(address);
			if ((driver.getWindowHandles().size() - 1) % tabNumbers == 0 || i == list.size()-1) { 
				for (String w : driver.getWindowHandles()) {
					if (w.equals(mainWindow)){
						continue;
					}
					driver.switchTo().window(w);
					if(waiter.until(driver)){
						File[] images = imageSave(mainWindow, w,by);
						new Thread(() -> {
							MergeInfo mergeInfo = concurrentHashMap.get(w);
							mergeImage(images, mergeInfo,0);
							concurrentHashMap.remove(w);
							MyUtils.deleteFiles(images);
						}).start();
					}
				}
				driver.switchTo().window(mainWindow);
			}
		}
    }

	public void asyncSaveImageYX(String mainWindow,Waiter waiter,List<String> list,By by){
		driver.switchTo().window(mainWindow);

		// 새창에서 열기
		for(int i =0; i<list.size();i++) {
			String address = list.get(i);
			js.executeScript(address);
			if ((driver.getWindowHandles().size() - 1) % tabNumbers == 0 || i == list.size()-1) { 
				for (String w : driver.getWindowHandles()) {
					if (w.equals(mainWindow)){
						continue;
					}
					driver.switchTo().window(w);
					if(waiter.until(driver)){
						File[][] images = imageSave2D(mainWindow, w,by);
						new Thread(() -> {
							MergeInfo mergeInfo = concurrentHashMap.get(w);
							mergeImage(images, mergeInfo);

							concurrentHashMap.remove(w);
							MyUtils.deleteFiles(images);
						}).start();
					}else{
						driver.close();
					}
				}
				driver.switchTo().window(mainWindow);
			}
		}
    }
}
