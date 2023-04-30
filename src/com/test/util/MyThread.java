package com.test.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MyThread extends Thread{
	private WebDriver driver;
	private WebElement webElement;
	private JavascriptExecutor js;
	public MyThread(WebDriver driver,String id, String pw, String[] excutableJS) {
		super();
		this.driver = driver;
		this.js = (JavascriptExecutor)driver;
		System.out.println(this.driver.toString());
	}

	private void init() {
		driver.get(getName());
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		webElement = driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]"));
		int height = webElement.getSize().getHeight();
		System.out.println(height);
		
		int n = (int)height/400 + 1;
		File[] scrFile = new File[n];
		for (int k = 0; k < n; k++) {
			scrFile[k] = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//			copy(scrFile[i], new File("c:/Temp/screenshot" + i + ".png")); // 주석 예정
			js.executeScript("window.scrollTo(0," + 400 * (k + 1) + ")");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mergeImage(scrFile,driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/div[1]/table/tbody/tr[4]/td[1]")).getText());
		driver.close();
	}
	
	
	private void mergeImage(File[] images,String fileName) {
		try {
			BufferedImage[] is = new BufferedImage[images.length];

			int width = 0;
			int height = 0;
			for (int i = 0; i < is.length; i++) {
				is[i] = ImageIO.read(images[i]);
				width = Math.max(width, is[i].getWidth());
				height += is[i].getHeight();
			}

			BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				graphics.drawImage(is[i], 0, tempHeight - (i * (400 - 20)), null);
				tempHeight += is[i].getHeight();
			}

			ImageIO.write(mergedImage, "png", new File("c:/Temp/"+fileName+".png"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
