package com.gek.and.project4.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class DateUtil {
	public static final DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat dfHM = new SimpleDateFormat("HH:mm");
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

	public static String getFormattedHM(Date d) {
		return d != null ? dfHM.format(d) : "";
	}

	public static String getFormattedHM(int minutes) {
		if (minutes < 0) {
			return formatHM(0, 0);
		}
		int hours = (int) Math.floor(minutes / 60);
		int mins = minutes - (hours * 60);
		return formatHM(hours, mins);
	}

	public static String getFormattedHMDecimal(int minutes) {
		if (minutes < 0) {
			return formatHM(0.0);
		}
		int hours = (int) Math.floor(minutes / 60);
		int mins = minutes - (hours * 60);
		double hoursDecimal = (double)hours + (double)mins / 60.0;
		return formatHM(hoursDecimal);
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

	public static Integer getMinutes(Calendar cBreak) {
		int breakHours = cBreak.get(Calendar.HOUR_OF_DAY) * 60;
		int breakMinutes = cBreak.get(Calendar.MINUTE);

		return breakHours + breakMinutes;
	}

	private static String formatHM(int hours, int minutes) {
		return String.format("%02d:%02d", hours, minutes);
	}

	private static String formatHM(double hours) {
		return String.format("%.2f", hours);
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

	public static Optional<Date> parseDate(String day) {
		try {
			return Optional.of(dfDate.parse(day));
		}
		catch (ParseException e) {
			return Optional.empty();
		}
	}

	public static Optional<Date> parseDateTime(String dateTime) {
		try {
			return Optional.of(dfDateTime.parse(dateTime));
		}
		catch (ParseException e) {
			return Optional.empty();
		}
	}

	public static Optional<Integer> parseHM(String hm, int position) {
		try {
			String[] parts = hm.split(":");
			return Optional.of(Integer.parseInt(parts[position]));
		}
		catch (Exception e) {
			return Optional.empty();
		}
	}

	public static Date getNextDay(Date theDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDay);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);

		return cal.getTime();
	}

	public static Date getDayBegin(Date theDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(theDay);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		return c.getTime();
	}

	public static Date getDayEnd(Date theDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(theDay);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);

		return c.getTime();
	}
}
