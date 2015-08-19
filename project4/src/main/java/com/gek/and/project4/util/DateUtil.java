package com.gek.and.project4.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat dfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat dfDay = new SimpleDateFormat("d. MMM");
	public static final DateFormat dfWeek = new SimpleDateFormat("w");
	public static final DateFormat dfMonth = new SimpleDateFormat("MMM yyyy");
	public static final DateFormat dfYear = new SimpleDateFormat("yyyy");
	public static final DateFormat dfFileNameToday = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat dfFileNameWeek = new SimpleDateFormat("w");
	public static final DateFormat dfFileNameMonth = new SimpleDateFormat("yyyy_MM");
	public static final DateFormat dfFileNameYear = new SimpleDateFormat("yyyy");
	public static final DateFormat dfDayFull = new SimpleDateFormat("EEE, d. MMM yyyy");

	public static String getFormattedDateTime(Date d) {
		return dfDateTime.format(d);
	}

	public static String getFormattedDate(Date d) {
		return dfDate.format(d);
	}
	
	public static String getFormattedTime(Date d) {
		return d != null ? dfTime.format(d) : "";
	}
	
	public static String getFormattedHM(int minutes) {
		int hours = (int) Math.floor(minutes / 60);
		int mins = minutes - (hours * 60);
		return formatHM(hours, mins);
	}
	
	public static String getFormattedDay(Calendar cal) {
		return dfDay.format(cal.getTime());
	}
	
	public static String getFormattedDayFull(Date day) {
		return dfDayFull.format(day);
	}
	
	public static String getFormattedWeek(Calendar cal) {
		return "KW " + dfWeek.format(cal.getTime());
	}
	
	public static String getFormattedMonth(Calendar cal) {
		return dfMonth.format(cal.getTime());
	}
	
	public static String getFormattedYear(Calendar cal) {
		return dfYear.format(cal.getTime());
	}
	
	public static String getFormattedFileNameToday(Calendar cal) {
		return "Tag_" + dfFileNameToday.format(cal.getTime());
	}
	
	public static String getFormattedFileNameWeek(Calendar cal) {
		return "Woche_" + dfFileNameWeek.format(cal.getTime());
	}
	public static String getFormattedFileNameMonth(Calendar cal) {
		return "Monat_" + dfFileNameMonth.format(cal.getTime());
	}
	public static String getFormattedFileNameYear(Calendar cal) {
		return "Jahr_" + dfFileNameYear.format(cal.getTime());
	}
	
	public static Integer getMinutes(Date from, Date to) {
		if (from == null || to == null) {
			return Integer.valueOf(0);
		}
		else {
			long millis = to.getTime() - from.getTime();
			return Integer.valueOf((int)millis / 1000 / 60);
		}
	}
	
	private static String formatHM(int hours, int minutes) {
		return String.format("%02d:%02d", hours, minutes);
	}

	public static Calendar combineDateAndTime(Calendar date, Calendar time) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, date.get(Calendar.YEAR));
		c.set(Calendar.MONTH, date.get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR));
		
		c.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, time.get(Calendar.SECOND));
		return c;
	}
}
