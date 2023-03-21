package com.mannash.simcardvalidation.threads;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CardReaderTest {
	
	private static String formatN(String paramString, int paramInt) {
		String str = "";
		int i = paramString.length();
		if (i >= paramInt) {
			str = paramString;
		} else {
			for (byte b = 0; b < paramInt - i; b++)
				str = str + "0";
			str = str + paramString;
		}
		return str;
	}
	
	public static String getdate(int paramInt) {
		String str = "yyyyMMdd";
		if (paramInt == 2) {
			str = "yyMMdd";
		} else if (paramInt == 3) {
			str = "HH";
		} else if (paramInt == 4) {
			str = "dd-MM HH";
		} else if (paramInt == 5) {
			str = "yyyy-MM-dd";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
		Date date = new Date();
		return simpleDateFormat.format(date);
	}
	
	public static void main(String[] args) {
		
//		LocalDate date = LocalDate.now();
//		System.out.println("date : "+date.getYear()+date.getMonthValue()+date.getDayOfMonth());
		
		Calendar calendar = Calendar.getInstance();
		String str2 = formatN("" + calendar.get(1), 4) + formatN("" + (calendar.get(2) + 1), 2)
				+ formatN("" + calendar.get(5), 2);
		String str3 = formatN("" + calendar.get(11), 2) + formatN("" + calendar.get(12), 2)
				+ formatN("" + calendar.get(13), 2);
		String str4 = getdate(1);
		String str5 = str4.substring(0, 6);
		
//		System.out.printf("" + calendar.get(1), 4);
		
		System.out.println("calender.get(2) : "+calendar.get(2)+1);
		System.out.println("str2 : "+str2);
		System.out.println("str3 : "+str3);
		System.out.println("str4 : "+str4);
		System.out.println("str5 : "+str5);
	}

}
