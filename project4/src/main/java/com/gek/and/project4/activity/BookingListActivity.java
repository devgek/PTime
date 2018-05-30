package com.gek.and.project4.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.async.ExportGenerator;
import com.gek.and.project4.async.ExportImporter;
import com.gek.and.project4.async.ImportResult;
import com.gek.and.project4.async.SummaryLoader.SummaryLoaderTarget;
import com.gek.and.project4.dao.BookingDao;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.menu.PeriodActionProvider.PeriodActionProviderListener;
import com.gek.and.project4.menu.ProjectActionProvider.ProjectActionProviderListener;
import com.gek.and.project4.service.BookingImportService;
import com.gek.and.project4.types.PeriodType;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class BookingListActivity extends AppCompatActivity implements ProjectActionProviderListener, PeriodActionProviderListener, SummaryLoaderTarget, ExportImporter.ExportImporterTarget {
	private static final String IMPORT_PROP_FILE_NAME = "import_file";
	private static final String IMPORT_PROPERTIES_FILE_NAME = "ptime-import.properties";
	private static final String PERIOD_ITEM_POSITION = "period_item_position";
	private static final String PROJECT_ITEM_POSITION = "project_item_position";
	private int periodActionPosition;
	private int projectActionPosition;

	//first action selection after create is ignored
	private boolean firstActionSelection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.booking_list_toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.firstActionSelection = true;
		
//		WorkaroundActionOverflow.execute(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.booking, menu);

//		MenuItem periodItem = menu.findItem(R.id.menu_period_spinner);
//		Spinner periodSpinner = (Spinner) MenuItemCompat.getActionView(periodItem);
	    
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		if (itemId == R.id.action_export) {
			exportBookings();
			return true;
		}
		if (itemId == R.id.action_import) {
			importBookings();
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(PERIOD_ITEM_POSITION)) {
			periodActionPosition = savedInstanceState.getInt(PERIOD_ITEM_POSITION);
		}
		if (savedInstanceState.containsKey(PROJECT_ITEM_POSITION)) {
			projectActionPosition = savedInstanceState.getInt(PROJECT_ITEM_POSITION);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Serialize the current dropdown position.
		outState.putInt(PERIOD_ITEM_POSITION, periodActionPosition);
		outState.putInt(PROJECT_ITEM_POSITION, projectActionPosition);
	}

	@Override
	public void onProjectActionItemSelected(int position) {
		this.projectActionPosition = position;
		switchToFragment();
	}

	@Override
	public void onPeriodActionItemSelected(int position) {
		this.periodActionPosition = position;
		PeriodType periodType = PeriodType.fromInt(periodActionPosition);
		if (PeriodType.SELECT_MONTH.equals(periodType)) {

		}
		else {
			switchToFragment();
		}
	}

	public void switchToFragment() {
		if (this.firstActionSelection) {
			this.firstActionSelection = false;
			return;
		}
		Project4App.getApp(this).setLastBookingList(null);

		PeriodType periodType = PeriodType.fromInt(periodActionPosition);
		Fragment fragment = new  BookingListFragment(periodType, projectActionPosition);
		getFragmentManager().beginTransaction().replace(R.id.booking_list_frame, fragment).commit();
	}
	
	public void onExportGenerationOk(String exportFileName) {
		Intent sendEmail= new Intent(Intent.ACTION_SEND);
		sendEmail.setType("text/csv");
		sendEmail.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(FileUtil.getInternalFile(this, exportFileName))); 
		
		startActivity(Intent.createChooser(sendEmail, exportFileName + " senden:"));	
	}
	
	public void onExportGenerationNotOk() {
		Toast.makeText(getApplicationContext(), "Daten für Export konnten nicht generiert werden", Toast.LENGTH_SHORT).show();
	}

	private void exportBookings() {
		List<Booking> bookingList = Project4App.getApp(this).getLastBookingList();
		if (bookingList == null || bookingList.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Keine Daten für Export vorhanden.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		ExportGenerator generator = new ExportGenerator();
		generator.execute(new Object[] {this, bookingList, getExportFileName()});
	}

	private void importBookings() {
		File sd = Environment.getExternalStorageDirectory();
		File importPropertiesFile = new File(sd, IMPORT_PROPERTIES_FILE_NAME);
		if (!importPropertiesFile.exists()) {
			Toast.makeText(getApplicationContext(), "Keine Konfigdatei für Import vorhanden.", Toast.LENGTH_SHORT).show();
			return;
		}
		Properties importProperties = null;
		try {
			importProperties = FileUtil.loadProperties(importPropertiesFile);
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Konfigdatei für Import konnte nicht gelesen werden.", Toast.LENGTH_SHORT).show();
			return;
		}
		String importFileName = importProperties.getProperty(IMPORT_PROP_FILE_NAME);
		if (importFileName == null || importFileName.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Property 'import_file' nicht vorhanden.", Toast.LENGTH_SHORT).show();
			return;
		}

		File importFile = new File(sd, importFileName);

		List<String> importLines = new ArrayList<>();
		BufferedReader b = null;
		try {
			b = new BufferedReader(new FileReader(importFile));
			String readLine = "";

			while ((readLine = b.readLine()) != null) {
				importLines.add(readLine);
			}
		}
		catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Fehler beim Lesen des Importfiles, Import abgebrochen.", Toast.LENGTH_SHORT).show();
			return;
		}

		ExportImporter importer = new ExportImporter();
		importer.execute(new Object[] {this, this, importLines, importProperties});
	}

	private String getExportFileName() {
		StringBuffer buf = new StringBuffer("export_");
		
		Calendar cal = Project4App.getApp(this).getSummary().getInitDate();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		switch(this.periodActionPosition) {
			case 0: buf.append(DateUtil.getFormattedFileNameToday(cal)); break;
			case 1: buf.append(DateUtil.getFormattedFileNameWeek(cal)); break;
			case 2: buf.append(DateUtil.getFormattedFileNameMonth(cal)); break;
			case 3: buf.append(DateUtil.getFormattedFileNameYear(cal)); break;
			case 4: buf.append(DateUtil.getFormattedFileNameYear(Project4App.getApp(this).getSummary().getPriorYearDate())); break;
			case 5: buf.append(DateUtil.getFormattedFileNameMonth(Project4App.getApp(this).getSummary().getPriorMonthDate())); break;
			default:break;
		}
		
		buf.append(".csv");
		
		return buf.toString();
	}

	protected void addBooking() {
		Booking booking = new Booking();
		Calendar now = Calendar.getInstance();
		booking.setFrom(now.getTime());
		booking.setTo(now.getTime());
		Project4App.getApp(this).setEditBooking(booking);
		
		Intent intent = new Intent(this, BookingDetailActivity.class);
		startActivityForResult(intent, 3000);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 3000) {
				boolean reloadList = data.getBooleanExtra("reloadList", false);
				if (reloadList) {
					Project4App.getApp(this).setLastBookingList(null);
				}
			}
			if (requestCode == 3010) {
				Project4App.getApp(this).setLastBookingList(null);
			}
		}
	}

	@Override
	public void onPostSummaryLoad() {
		System.out.println("BookingListActivity::onPostSummaryLoad");
	}

	@Override
	public void onPostExportImporter(ImportResult result) {
		System.out.println("BookingListActivity::onPostExportImporter");
		if (result.getResult().equals(ImportResult.ImportResultType.DONE) && result.getImports().size() > 0) {
			Project4App.getApp(this).setImportList(result.getImports());
			Intent intent = new Intent(this, BookingImportActivity.class);
			startActivityForResult(intent, 3010);
		}
	}
}
