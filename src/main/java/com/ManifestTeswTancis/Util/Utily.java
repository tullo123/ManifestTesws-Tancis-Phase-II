package com.ManifestTeswTancis.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utily {
	private static String convertDate(Date date) {
		if (date == null)
			return null;

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	private static String convertDate2(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS");
		return sdf.format(date).toUpperCase();
	}

	public static String sysdate() {
		return convertDate(new Date());
	}
	
	public static String sysdate2() {
		return convertDate2(new Date());
	}

}
