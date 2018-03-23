package com.gek.and.project4.async;

import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.gek.and.project4.activity.BookingListActivity;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.util.BookingUtil;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ExportGenerator extends AsyncTask<Object, Void, Boolean> {
	private BookingListActivity parentActivity;
	List<Booking> bookingList;
	String exportFileName;
	
	@Override
	protected Boolean doInBackground(Object... params) {
		Thread.currentThread().setName("ExportGenerator");
		
		parentActivity = (BookingListActivity) params[0];
		bookingList = (List<Booking>) params[1];
		exportFileName = (String) params[2];

		String userName = PreferenceManager.getDefaultSharedPreferences(parentActivity).getString("setting_user_name", "");
		boolean showDuration = PreferenceManager.getDefaultSharedPreferences(parentActivity).getBoolean("setting_export_showDuration", false);
		boolean showSummaryDecimal = PreferenceManager.getDefaultSharedPreferences(parentActivity).getBoolean("setting_export_showSummaryDecimal", false);
		boolean sortAscending = PreferenceManager.getDefaultSharedPreferences(parentActivity).getBoolean("setting_export_sortAscending", false);
		if (sortAscending) {
		//needs sdk version 24 at least
//			Collections.sort(bookingList, (o1, o2) -> o1.getFrom().compareTo(o2.getFrom()));
			Collections.sort(bookingList, new Comparator<Booking>() {
				@Override
				public int compare(Booking o1, Booking o2) {
					return o1.getFrom().compareTo(o2.getFrom());
				}
			});
		}

		try {
			File exportFile = FileUtil.getInternalFile(parentActivity, exportFileName);
			FileOutputStream fos = new FileOutputStream(exportFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);

			if (!userName.isEmpty()) {
				bw.write(userName);
				bw.newLine();
				bw.newLine();
			}

			bw.write("Kunde;Projekt;Datum;Von;Bis");
			if (showDuration) {
				bw.write(";Dauer");
			}
			bw.write(";Pause;Gesamt;Tätigkeiten");
			bw.newLine();
			bw.newLine();
			
			ProjectService projectService = Project4App.getApp(parentActivity).getProjectService();

			int totalMinutes = 0;

			for (Booking booking : bookingList) {
				totalMinutes += BookingUtil.getDuration(booking);

				Project p = projectService.getProject(booking.getProjectId());
				StringBuffer buf = new StringBuffer();
				//Kunde
				buf.append("\"" + (p != null ? p.getCompany() : "") + "\"");
				buf.append(";");
				//Projekt
				buf.append("\"" + (p != null ? p.getTitle() : "") + "\"");
				buf.append(";");
				//Datum
				buf.append("\"" + DateUtil.getFormattedDate(booking.getFrom()) + "\"");
				buf.append(";");
				//Von
				buf.append("\"" + DateUtil.getFormattedHM(booking.getFrom()) + "\"");
				buf.append(";");
				//Bis
				StringBuffer append = buf.append("\"" + (booking.getTo() != null ? DateUtil.getFormattedHM(booking.getTo()) : "") + "\"");
				buf.append(";");
				//Dauer
				if (showDuration) {
					buf.append("\"" + (booking.getMinutes() != null ? DateUtil.getFormattedHM(booking.getMinutes()) : "") + "\"");
					buf.append(";");
				}
				//Pause
				buf.append("\"" + (booking.getMinutes() != null ? DateUtil.getFormattedHM(booking.getBreakHours() * 60 + booking.getBreakMinutes()) : "") + "\"");
				buf.append(";");
				//Gesamt
				if (showSummaryDecimal) {
					buf.append("\"" + (booking.getMinutes() != null ? DateUtil.getFormattedHMDecimal(booking.getMinutes() - (booking.getBreakHours() * 60 + booking.getBreakMinutes())) : "") + "\"");
				}
				else {
					buf.append("\"" + (booking.getMinutes() != null ? DateUtil.getFormattedHM(booking.getMinutes() - (booking.getBreakHours() * 60 + booking.getBreakMinutes())) : "") + "\"");
				}
				buf.append(";");
				//Tätigkeiten
				buf.append("\"" + (booking.getNote() != null ? booking.getNote() : "") + "\"");

				bw.write(buf.toString());
				bw.newLine();
			}
			StringBuffer total = new StringBuffer("Gesamt:;;;;;;");
			if (showSummaryDecimal) {
				total.append("\"" + DateUtil.getFormattedHMDecimal(totalMinutes) + "\"");
			}
			else {
				total.append("\"" + DateUtil.getFormattedHM(totalMinutes) + "\"");
			}
			bw.newLine();
			bw.write(total.toString());
			
			bw.close();
		}
		catch (Exception e) {
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}

	@Override
	protected void onPostExecute(Boolean generated) {
		if (generated) {
			parentActivity.onExportGenerationOk(exportFileName);
		}
		else {
			parentActivity.onExportGenerationNotOk();
		}
	}

}
