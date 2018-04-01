package com.gek.and.project4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gek.and.geklib.draganddroplist.DragNDropListView;
import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.BookingListArrayAdapter;
import com.gek.and.project4.listadapter.BookingListReadOnlyArrayAdapter;
import com.gek.and.project4.listadapter.ProjectManagementArrayAdapter;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.service.BookingService;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.util.L;

import java.util.List;

public class BookingImportActivity extends AppCompatActivity {
	private BookingListReadOnlyArrayAdapter adapter;
	private BookingListActivity parentActivity;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.booking_import, menu);

//		MenuUtil.colorMenu(menu, getResources().getColor(R.color.g_ColorActionItemsTint));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int i = item.getItemId();
		if (i == R.id.action_save) {
			doImport();
			finish();
			return true;
			// Respond to the action bar's Up/Home button
		} else if (i == android.R.id.home) {//	        NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.booking_import_list);
		
		ListView listView = (ListView) findViewById(R.id.import_list_view);

		BookingListArrayAdapter bookingListAdapter = new BookingListArrayAdapter(R.layout.booking_row, this);

		bookingListAdapter.addAll(Project4App.getApp(this).getImportList());
		boolean showNote = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("setting_bookings_showNote", false);
		bookingListAdapter.setVisibilityLine3(showNote ? View.VISIBLE : View.GONE);

		listView.setAdapter(bookingListAdapter);

		Toolbar toolbar = (Toolbar) findViewById(R.id.booking_import_toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(R.string.title_booking_import);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void doImport() {
		L.d("BookingImportActivity::", "doImport");
		BookingService bookingService = Project4App.getApp(this).getBookingService();
		boolean importOk = bookingService.importBookings(Project4App.getApp(this).getImportList());
		if (importOk) {
			Toast.makeText(this, "Buchungen wurden importiert.", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(this, "Buchungen wurden nicht importiert.", Toast.LENGTH_SHORT).show();
		}
	}
	

}
