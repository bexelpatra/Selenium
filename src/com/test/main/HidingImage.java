package com.test.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HidingImage {

	public static void main(String[] args) throws Exception {
		HidingImage i = new HidingImage();
		File dir = new File("C:\\Temp");
		File to = new File("C:/Temp/overlap.png");
		File uncovered = new File("C:/Temp/uncovered.png");
//		i.fileConnet(dir.listFiles(pathname -> pathname.getName().contains("1") ), to);
		i.uncoverFile(to, new File("c:/Temp/uncover.png"), 21414);
	}
	public void fileConnet(File[] origin, File to) throws Exception {
		FileInputStream[] input = new FileInputStream[origin.length];

		FileOutputStream oss = new FileOutputStream(to);

		System.out.println("file size ");
		for (int i = 0; i < origin.length; i++) {
			input[i] = new FileInputStream(origin[i]);
			System.out.println(input[i].getChannel().size()); // origin[i].length()와 동일

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
//		fileChanger(to, new File("c:/Temp/uncover.png"), origin[0].length());
	}
	
	public void uncoverFile(File origin, File to, long remove) {
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
