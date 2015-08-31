package com.gek.and.project4.util;

import com.gek.and.project4.entity.Booking;

import java.util.List;

public class SummaryUtil {
	public static int getMinutes(List<Booking> bookingList, Long projectId) {
		int minutes = 0;
		
		for (Booking booking : bookingList) {
			if (booking.getMinutes() != null) {
				minutes += booking.getMinutes();
			}
		}
		
		return minutes;
	}
}
