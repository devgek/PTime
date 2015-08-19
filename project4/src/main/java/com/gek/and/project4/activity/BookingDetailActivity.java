package com.gek.and.project4.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.fragment.DatePickerFragment;
import com.gek.and.project4.fragment.ProjectSelectionFragment;
import com.gek.and.project4.fragment.ProjectSelectionFragment.ProjectSelectionDialogListener;
import com.gek.and.project4.fragment.TimePickerFragment;
import com.gek.and.project4.fragment.TimePickerFragment.OnTimeSetListener;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.view.ProjectView;

public class BookingDetailActivity extends Activity implements ProjectSelectionDialogListener, OnTimeSetListener{
	private TextView headLine;
	private EditText from;
	private EditText to;
	private EditText duration;
	private ProjectView projectView;
	private EditText note;
	
	private Booking theBooking;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.booking_edit, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_cancel) {
			cancel();
			return true;
		} else if (itemId == R.id.action_discard) {
			confirmDeleteBooking();
			return true;
		} else if (itemId == R.id.action_save) {
			saveBooking();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.booking_detail);
		
		this.theBooking = Project4App.getApp(this).getEditBooking();
		
		headLine = (TextView) findViewById(R.id.bookingDetailHeadLine);
		headLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDatePickerDialog((TextView) v);
			}
		});
		
		from = (EditText) findViewById(R.id.bookingDetailFrom);
		from.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimePickerDialog((EditText) v);
			}
		});
		
		to = (EditText) findViewById(R.id.bookingDetailTo);
		to.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimePickerDialog((EditText) v);
			}
		});

		duration = (EditText) findViewById(R.id.bookingDetailDuration);

		projectView	= (ProjectView) findViewById(R.id.bookingDetailProject);
		projectView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProjectSelectionDialog(v);
			}
		});
		
		note	= (EditText) findViewById(R.id.bookingDetailNote);
		
		prepareData();
		
		if (isModeNew()) {
			getActionBar().setTitle(R.string.title_booking_new);
		}
		else {
			getActionBar().setTitle(R.string.title_booking_edit);
		}
			
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}


	private boolean isModeNew() {
		return theBooking.getId() == null;
	}

	private void prepareData() {
		if (theBooking != null) {
			headLine.setText(DateUtil.getFormattedDayFull(theBooking.getFrom()));
			Calendar cDay = Calendar.getInstance();
			cDay.setTime(theBooking.getFrom());
			headLine.setTag(cDay);
			
			Calendar cFrom = Calendar.getInstance();
			if (theBooking.getFrom() != null) {
				cFrom.setTime(theBooking.getFrom());
			}
			from.setText(DateUtil.getFormattedTime(cFrom.getTime()));
			from.setTag(cFrom);
			
			Calendar cTo = Calendar.getInstance();
			if (theBooking.getTo() != null) {
				cTo.setTime(theBooking.getTo());
			}
			to.setText(DateUtil.getFormattedTime(cTo.getTime()));
			to.setTag(cTo);
			
			duration.setText(DateUtil.getFormattedHM(theBooking.getMinutes()));
			
			if (theBooking.getProjectId() != null) {
				prepareDataProject(theBooking.getProjectId());
			}
			
			note.setText(theBooking.getNote() != null ? theBooking.getNote() : "");
		}
	}

	private void updateDuration() {
		Calendar cFrom = (Calendar) from.getTag();
		Calendar cTo = (Calendar) to.getTag();
		Integer minutes = DateUtil.getMinutes(cFrom.getTime(), cTo.getTime());
		
		duration.setText(DateUtil.getFormattedHM(minutes));
	}
	
	private void prepareDataProject(Long projectId) {
		Project p = Project4App.getApp(this).getProjectService().getProject(projectId);
		projectView.setCustomer(p.getCompany());
		projectView.setTitle(p.getTitle());
		projectView.setColor(Color.parseColor(p.getColor()));
		
		projectView.setTag(projectId);
	}
	
	private void saveBooking() {
		Calendar now = Calendar.getInstance();
		Calendar cDay = (Calendar) this.headLine.getTag();
		Calendar cFrom = (Calendar) this.from.getTag();
		Calendar cTo = (Calendar) this.to.getTag();
		
		Calendar cFromCombined = DateUtil.combineDateAndTime(cDay, cFrom);
		Calendar cToCombined = DateUtil.combineDateAndTime(cDay, cTo);
		
		if (cFromCombined.compareTo(cToCombined) > 0) {
			Toast.makeText(this, "Stop-Zeit darf nicht kleiner als Start-Zeit sein.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (cToCombined.compareTo(now) > 0) {
			Toast.makeText(this, "Änderungen sind nur in der Vergangenheit möglich.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Long projectId = (Long) this.projectView.getTag();
		if (projectId == null) {
			Toast.makeText(this, "Ein Projekt muss ausgewählt werden.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		this.theBooking.setFrom(cFromCombined.getTime());
		
		this.theBooking.setTo(cToCombined.getTime());
		
		this.theBooking.setProjectId(projectId);
		
		String note = this.note.getText().toString();
		this.theBooking.setNote(note);
		
		Project4App.getApp(this).getBookingService().updateBooking(this.theBooking);
		
		goBackWithResult(RESULT_OK, true);
	}
	
	private void cancel() {
		finish();
	}
	
	private void goBackWithResult(int result, boolean reloadList) {
		Intent back = new Intent(getApplicationContext(), BookingListActivity.class);
		back.putExtra("reloadList", reloadList);
		setResult(result, back);
		finish();
	}
	
	private boolean isRunningBooking() {
		Booking runningBooking = Project4App.getApp(this).getSummary().getRunningNow();
		return runningBooking != null && runningBooking.getId().equals(this.theBooking.getId());
	}

	public void confirmDeleteBooking() {
//		if (!Project4App.getApp(this).isPro()) {
//			Toast.makeText(this, "Diese Funktion ist nur in der Pro-Version verfügbar.", Toast.LENGTH_SHORT).show();
//			return;
//		}
		
		if (isRunningBooking()) {
			Toast.makeText(this, "Die gerade laufende Zeitbuchung kann nicht gelöscht werden.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Buchung löschen");
		ad.setMessage("Die Zeitbuchung wird gelöscht.");
		ad.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteBooking();
						arg0.cancel();

					}
				});
		ad.setNegativeButton("Abbrechen",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.cancel();
					}
				});
		AlertDialog ad_echt = ad.create();
		ad_echt.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK) {
//			//project selected from dialog
//			if (requestCode == 11) {
//				Long projectId = data.getLongExtra("selectedProjectId", -1);
//				if (projectId > -1) {
//					projectView.setTag(projectId);
//					prepareDataProject(projectId);
//				}
//			}
//		}
	}
	
	private void deleteBooking() {
		boolean deleted = Project4App.getApp(this).getBookingService().deleteBooking(this.theBooking);
		if (!deleted) {
			Toast.makeText(this, "Zeitbuchung konnte nicht gelöscht werden.", Toast.LENGTH_SHORT).show();
		}
		else {
			goBackWithResult(RESULT_OK, true);
		}
	}
	
	private void showDatePickerDialog(TextView v) {
	    DialogFragment newFragment = new DatePickerFragment(v);
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog(final EditText v) {
	    DialogFragment newFragment = new TimePickerFragment(v, this);
	    newFragment.show(getFragmentManager(), "timePicker");
	}

	private void showProjectSelectionDialog(View v) {
//		Intent projectSelectionDialog = new Intent(this, ProjectSelectionActivity.class);
//		startActivityForResult(projectSelectionDialog, 11);
		ProjectSelectionFragment projectSelectionFragment = new ProjectSelectionFragment();
		projectSelectionFragment.show(getFragmentManager(), "projectSelector");
	}

	@Override
	public void onProjectSelected(Long projectId) {
		if (projectId > -1) {
			projectView.setTag(projectId);
			prepareDataProject(projectId);
		}
	}

	@Override
	public void onTimeSet() {
		updateDuration();
	}
	
}
