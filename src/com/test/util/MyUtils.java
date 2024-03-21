package com.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {

	public static Date toDate(SimpleDateFormat format,String str) throws ParseException {
		return format.parse(str);
	}
}
