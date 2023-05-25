package com.test.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ImageMerge {
	private WebDriver driver;
	private JavascriptExecutor js;

	private WebElement webElement;
	private String saveDir = "src/images/test/";
	private String fileName = "";
	
	private int scrollDown =0;
	private int elementHeight = 0;
	public ImageMerge(WebDriver driver) {
		super();
		this.driver = driver;
		js = (JavascriptExecutor) driver;
		this.scrollDown=driver.manage().window().getSize().getHeight()/2;
	}
	public File[] imageSave(String mainWindow, String windowName) {
		driver.switchTo().window(windowName);
		webElement = driver.findElement(By.xpath("/html/body"));
		int windowHeight = driver.manage().window().getSize().height;
		int contentHeight = webElement.getSize().getHeight();
		this.elementHeight = contentHeight;
		
		// 외부에서 사용할수있도록 설정
		setFileName();
		
		int i =1;
		while(contentHeight - (windowHeight + (scrollDown)*(i-1) ) >= 0){
			i+=1;
		}
		System.out.printf("window height : %d , content height : %d \n",windowHeight, contentHeight);
		File[] scrFile = new File[i];
		Object ob = null;
		int imgHeight = 0;
		for (int k = 0; k < i; k++) {
//			scrFile[k] = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			scrFile[k] = ((TakesScreenshot) webElement).getScreenshotAs(OutputType.FILE);
			js.executeScript("window.scrollTo(0," + (getImageHeight(scrFile[k]))* (k + 1) + ")");
//			js.executeScript("window.scrollTo(0," + (scrollDown)* (k + 1) + ")");
//			js.executeScript("window.scrollTo(0," + windowHeight * (k + 1) + ")");
			ob = js.executeScript("return window.scrollY");
			System.out.println(ob);
			sleep(10);
		}
		System.out.println("end");
		Arrays.stream(scrFile).forEach(t ->{
			try {
				copy(new FileInputStream(t), new FileOutputStream(new File(saveDir+"temp/"+t.getName())));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
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
//			height -= ((minus / 2 - contentHeight/14) * (is.length - 1));
			height -= (minus / 2) * (is.length - 1);

			BufferedImage mergedImage = new BufferedImage(width, height+200, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				int cutHeight = tempHeight;
				if(is.length-1 == i ) {
					cutHeight -= (height - elementHeight);
				}
				graphics.drawImage(is[i], 0, cutHeight, null);
				tempHeight += is[i].getHeight();
			}
			Arrays.stream(images).forEach(t -> t.delete());

			File saveFile = new File(saveDir+ fileName + ".png");
			int i =0 ;
			while(saveFile.exists()) {
				saveFile = new File(saveDir+ fileName + i +".png");
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
	private void setFileName() {
		StringBuilder sb = new StringBuilder();
		sb.append(driver.findElement(By.cssSelector("#container > div.content > div:nth-child(4) > table > tbody > tr:nth-child(2) > td:nth-child(4)")).getText().replaceAll(":", "-"));
		sb.append(".");
		sb.append(driver.findElement(By.cssSelector("#container > div.content > div:nth-child(4) > table > tbody > tr:nth-child(4) > td:nth-child(2)")).getText());
		this.fileName = sb.toString();
	}
}
