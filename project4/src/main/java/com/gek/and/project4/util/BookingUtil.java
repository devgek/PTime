package com.gek.and.project4.util;

import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;

import java.util.Calendar;

/**
 * Created by moo on 05.01.16.
 */
public class BookingUtil {
	public static int getBreakHours(Booking b) {
		return b.getBreakHours() != null ? b.getBreakHours() : 0;
	}

	public static int getBreakHoursMinutes(Booking b) {
		return b.getBreakHours() != null ? b.getBreakHours() * 60 : 0;
	}

	public static int getBreakMinutes(Booking b) {
		return b.getBreakMinutes() != null ? b.getBreakMinutes() : 0;
	}

	public static int getBreakTotal(Booking b) {
		int breakHours = 0;
		int breakMinutes = 0;
		if (b.getBreakHours() != null) {
			breakHours = b.getBreakHours() * 60;
		}
		if (b.getBreakMinutes() != null) {
			breakMinutes = b.getBreakMinutes();
		}

		return breakHours + breakMinutes;
	}

	public static int getDuration(Booking b) {
		return b.getMinutes() - getBreakTotal(b);
	}

	public static int getRunningMinutes(Booking b) {
		return DateUtil.getMinutes(b.getFrom(), Calendar.getInstance().getTime());
	}

}
