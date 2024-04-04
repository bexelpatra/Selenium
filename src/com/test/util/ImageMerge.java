package com.test.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	private int elementHeight = 0;
	private int lastScrollHeigh = 0;
	private int tabNumbers = 5;
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
	public File[] imageSave(String mainWindow, String windowName) {
		driver.switchTo().window(windowName);
		webElement = driver.findElement(By.xpath("/html/body"));
		int windowHeight = driver.manage().window().getSize().height;
		int contentHeight = webElement.getSize().getHeight();
		this.elementHeight = contentHeight;
//		setFileName();
		this.fileName = filenameSetter.setFileName(driver);

		System.out.printf("filename : %s  window height : %d , content height : %d \n",fileName,windowHeight, contentHeight);
		File[] scrFile = new File[contentHeight/(windowHeight/2)];
		Object ob = null;
		int totalCapturedImg = 0;
		int imgHeight = 0;
		int k=0;
		Object lastY = null;
		while (totalCapturedImg <= contentHeight+100) {
//			scrFile[k] = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			scrFile[k] = ((TakesScreenshot) webElement).getScreenshotAs(OutputType.FILE);
			imgHeight = getImageHeight(scrFile[k]);
			js.executeScript("window.scrollTo(0," + (imgHeight)* (k + 1) + ")");
			k+=1;
			totalCapturedImg +=imgHeight;
			ob = js.executeScript("return window.scrollY");
			if(lastY!= null && lastY.equals(ob)) break;
			lastY= ob;
			// System.out.println(ob);
			sleep(10);
		}
		
		lastScrollHeigh = Math.round(Float.parseFloat(String.valueOf(lastY)));
		driver.close();
		
		return scrFile;
	}
	private int getImageHeight(File file) {
		int result = 0;
		try {
			result = ImageIO.read(file).getHeight();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	public void mergeImage(File[] images, String fileName, int contentHeight) {
		int imagesCount = (int)Arrays.stream(images).filter(t -> t!=null).count();
		try {
			BufferedImage[] is = new BufferedImage[imagesCount];

			int width = 0;
			int height = 0;
			int minus = 0;
			for (int i = 0; i < is.length; i++) {
				if(images[i]==null) continue;
				is[i] = ImageIO.read(images[i]);
				width = Math.max(width, is[i].getWidth());
				height += is[i].getHeight();
				minus = is[i].getHeight();
			}

			BufferedImage mergedImage = new BufferedImage(width, elementHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				int cutHeight = tempHeight;
				if(is.length-1 == i ) {
//					cutHeight -= (height - elementHeight);
					cutHeight = lastScrollHeigh;
				}
				graphics.drawImage(is[i], 0, cutHeight, null);
				tempHeight += is[i].getHeight();
			}
			Arrays.stream(images).filter(t->t!=null).forEach(t -> t.delete());

			File saveFile = new File(saveDir+ fileName + ".png");
			int i =0 ;
			while(saveFile.exists()) {
				saveFile = new File(saveDir+ fileName + "_("+i +").png");
				i+=1;
			}
			ImageIO.write(mergedImage, "png",saveFile);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void sleep(long mil) {
		try {
			Thread.sleep(mil);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void copy(FileInputStream is, FileOutputStream os) {
		byte[] b = new byte[1024];
		int readData = 0;
		try {
			while((readData = is.read(b))>0) {
				os.write(b, 0, readData);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				is.close();
				os.close();
			} catch (Exception e2) {
				// TODO: handle exception
				System.out.println("close fail!");
			}
		}
	}
	public String getFileName() {
		return this.fileName;
	}
	
	public void saveImage(String mainWindow,List<String> list){
        File[] images = null;
		String fileName = "";
		driver.switchTo().window(mainWindow);

		// 새창에서 열기
		for(int i =0; i<list.size();i++) {
			String address = list.get(i);
			js.executeScript(address);
			if ((driver.getWindowHandles().size() - 1) % tabNumbers == 0 || i == list.size()-1) { 
				for (String w : driver.getWindowHandles()) {
					if (w.equals(mainWindow))
						continue; 
					images = imageSave(mainWindow, w);
					fileName = getFileName();
					mergeImage(images,fileName , 0);
					// temp 파일을 제때 지우자...
					MyUtils.deleteFiles(images);
				}
				driver.switchTo().window(mainWindow);
			}
		}
    }
	public void saveImage(String mainWindow,Waiter waiter,List<String> list){
        File[] images = null;
		String fileName = "";
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
						images = imageSave(mainWindow, w);
						fileName = getFileName();
						mergeImage(images,fileName , 0);
						MyUtils.deleteFiles(images);
					}
				}
				driver.switchTo().window(mainWindow);
				MyUtils.sleep(100);
			}
		}
    }
	public void asyncSaveImage(String mainWindow,Waiter waiter,List<String> list){
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
					if(waiter.until(driver)){
						File[] images = imageSave(mainWindow, w);
						String fileName = getFileName();
						new Thread(() -> {
							mergeImage(images,fileName , 0);
							// MyUtils.deleteFiles(images);
						}).start();
					}
				}
				driver.switchTo().window(mainWindow);
			}
		}
    }
}
