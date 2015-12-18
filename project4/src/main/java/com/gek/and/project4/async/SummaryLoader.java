package com.gek.and.project4.async;

import android.app.Activity;
import android.os.AsyncTask;

import com.gek.and.project4.Summary;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.service.BookingService;

import java.util.List;


public class SummaryLoader extends AsyncTask<Object, Void, Void> {
	private Activity parentActivity;
	private SummaryLoaderTarget target;
	
	@Override
	protected Void doInBackground(Object... params) {
		Thread.currentThread().setName("SummaryLoader");
		
		target = (SummaryLoaderTarget) params[0];
		parentActivity = (Activity) params[1];
		
		BookingService bookingService = Project4App.getApp(parentActivity).getBookingService();
		List<Booking> bookings = bookingService.getThisYear();
		
		Summary summary = Project4App.getApp(parentActivity).getSummary();
		
		synchronized(summary) {
			summary.loadNew(bookings);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		target.onPostSummaryLoad();
	}

	public interface SummaryLoaderTarget{
		public void onPostSummaryLoad();
	}
}
