package com.ManifestTeswTancis.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFormatter {

	public static Date getDateFromLocalDateTime(LocalDateTime localDateTime){
		if(localDateTime != null) {
			LocalDateTime ldt = LocalDateTime.ofInstant(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
			return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		}
		return null;
		}

		public static String getTeSWSLocalDate(LocalDateTime localDateTime){
			if(localDateTime != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
				return localDateTime.format(formatter);
			}
			return null;
		}

}
