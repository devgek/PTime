package com.gek.and.project4.async;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import android.os.AsyncTask;

import com.gek.and.project4.activity.BookingListActivity;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.FileUtil;


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
		
		try {
			File exportFile = FileUtil.getInternalFile(parentActivity, exportFileName);
			FileWriter fw = new FileWriter(exportFile, false);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("KUNDE;PROJEKT;START;ENDE;MINUTEN;NOTIZ");
			bw.newLine();
			
			ProjectService projectService = Project4App.getApp(parentActivity).getProjectService();

			for (Booking booking : bookingList) {
				Project p = projectService.getProject(booking.getProjectId());
				StringBuffer buf = new StringBuffer();
				buf.append(p != null ? p.getCompany() : "");
				buf.append(";");
				buf.append(p != null ? p.getTitle() : "");
				buf.append(";");
				buf.append(DateUtil.getFormattedDateTime(booking.getFrom()));
				buf.append(";");
				buf.append(booking.getTo() != null ? DateUtil.getFormattedDate(booking.getTo()) : "");
				buf.append(";");
				buf.append(booking.getMinutes() != null ? DateUtil.getFormattedHM(booking.getMinutes()) : "");
				buf.append(";");
				buf.append(booking.getNote() != null ? booking.getNote() : "");

				bw.write(buf.toString());
				bw.newLine();
			}
			
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
