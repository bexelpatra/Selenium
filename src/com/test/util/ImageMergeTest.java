package com.test.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageMergeTest {
	static String dir = "D:/class/personal/Selenium/src/images/test/temp";
		public static void main(String[] args) throws Exception{
		File[] images = new File(dir).listFiles(t->{return t.getName().indexOf("test")<0 && t.isFile();});
		Arrays.stream(images).forEach(t -> System.out.println(t.getName()) );
		int edit =0;
		int totalHeight=840;
		int start = 30;
		mergeImage(images, "test", totalHeight,0);
	}
	private static void mergeImage(File[] images, String fileName, int windowHeight,int edit) {
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
			height -= ((minus / 2) * (is.length - 1));

			BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();
			graphics.setBackground(Color.WHITE);
			int tempHeight = 0;
			for (int i = 0; i < is.length; i++) {
				int cut = i * (windowHeight/2);
				graphics.drawImage(is[i], 0, cut, null);
				tempHeight += cut;
			}
//			Arrays.stream(images).forEach(t -> t.delete());
			ImageIO.write(mergedImage, "png", new File(dir+ fileName+edit + ".png"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
