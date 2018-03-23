package com.gek.and.project4.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.gek.and.project4.activity.BookingListActivity;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.service.BookingImportService;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.util.BookingUtil;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;


public class ExportImporter extends AsyncTask<Object, Void, ImportResult> {
	private static final String IMPORT_PROP_HAS_HEADER = "import_has_header";
	private static final String IMPORT_PROP_IGNORE_PATTERN = "import_ignore_lines_starting_with";

	@Inject
	BookingImportService bookingImportService;

	//params[]
	private Activity parentActivity;
	private ExportImporterTarget target;
	private List<String> importLines;
	private Properties importProperties;

	@Override
	protected ImportResult doInBackground(Object... params) {
		Thread.currentThread().setName("ExportImporter");
		
		parentActivity = (Activity) params[0];
		target = (ExportImporterTarget) params[1];
		importLines = (List<String>) params[2];
		importProperties = (Properties) params[3];
		String hasHeaderString = importProperties.getProperty(IMPORT_PROP_HAS_HEADER, "true");
		String ignorePattern = importProperties.getProperty(IMPORT_PROP_IGNORE_PATTERN, "");

		Project4App app = Project4App.getApp(parentActivity);
		app.getServiceComponent().inject(this);

		ImportResult result = new ImportResult(ImportResult.ImportResultType.DONE);

		List<Booking> importList = bookingImportService.getBookingList(importLines, "true".equals(hasHeaderString), ignorePattern, app.getProjectService().getAllProjects(null));
		result.setImports(importList);

		return result;
	}

	@Override
	protected void onPostExecute(ImportResult importResult) {
		target.onPostExportImporter(importResult);
	}

	public interface ExportImporterTarget {
		public void onPostExportImporter(ImportResult result);
	}

}
