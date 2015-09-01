package com.gek.and.project4.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.gek.and.project4.async.SummaryLoader.SummaryLoaderTarget;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.menu.PeriodActionProvider.PeriodActionProviderListener;
import com.gek.and.project4.menu.ProjectActionProvider.ProjectActionProviderListener;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.FileUtil;

import java.util.Calendar;
import java.util.List;

public class BookingListActivity extends AppCompatActivity implements ProjectActionProviderListener, PeriodActionProviderListener, SummaryLoaderTarget {
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
		switchToFragment();
	}

	public void switchToFragment() {
		if (this.firstActionSelection) {
			this.firstActionSelection = false;
			return;
		}
		Project4App.getApp(this).setLastBookingList(null);
		
		Fragment fragment = new  BookingListFragment(periodActionPosition, projectActionPosition);
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

	private String getExportFileName() {
		StringBuffer buf = new StringBuffer("export_");
		
		Calendar cal = Project4App.getApp(this).getSummary().getInitDate();
		
		switch(this.periodActionPosition) {
			case 0: buf.append(DateUtil.getFormattedFileNameToday(cal)); break;
			case 1: buf.append(DateUtil.getFormattedFileNameWeek(cal)); break;
			case 2: buf.append(DateUtil.getFormattedFileNameMonth(cal)); break;
			case 3: buf.append(DateUtil.getFormattedFileNameYear(cal)); break;
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
		}
	}

	@Override
	public void onPostSummaryLoad() {
		System.out.println("BookingListActivity::onPostSummaryLoad");
	}

}
