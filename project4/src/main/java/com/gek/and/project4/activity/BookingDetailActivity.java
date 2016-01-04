package com.gek.and.project4.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.gek.and.project4.dialogcontroller.PeriodSummaryDialogController;
import com.gek.and.project4.dialogcontroller.ProjectSelectionDialogController;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.fragment.DatePickerFragment;
import com.gek.and.project4.fragment.ModalToolbarDialogFragment;
import com.gek.and.project4.fragment.TimePickerFragment;
import com.gek.and.project4.fragment.TimePickerFragment.OnTimeSetListener;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.MenuUtil;
import com.gek.and.project4.view.ProjectView;

import java.util.Calendar;
import java.util.regex.Pattern;

public class BookingDetailActivity extends AppCompatActivity implements OnTimeSetListener, ProjectSelectionDialogController.ProjectSelectionDialogListener {
//	private TextView headLine;
	private EditText day;
	private EditText from;
	private EditText to;
	private EditText mBreak;
	private EditText duration;
	private ProjectView projectView;
	private EditText note;
	
	private Booking theBooking;
	private Integer mDuration;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.booking_edit, menu);

//		MenuUtil.colorMenu(menu, getResources().getColor(R.color.g_ColorActionItemsTint));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
//		if (itemId == R.id.action_cancel) {
//			cancel();
//			return true;
//		}
	if (itemId == R.id.action_discard) {
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
		
//		headLine = (TextView) findViewById(R.id.bookingDetailHeadLine);
//		headLine.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showDatePickerDialog((EditText) v);
//			}
//		});

		day = (EditText) findViewById(R.id.bookingDetailDay);
		day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog((EditText) v);
			}
		});

		from = (EditText) findViewById(R.id.bookingDetailFrom);
		from.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimePickerDialog((EditText) v, false);
			}
		});
		
		to = (EditText) findViewById(R.id.bookingDetailTo);
		to.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimePickerDialog((EditText) v, false);
			}
		});

		mBreak = (EditText) findViewById(R.id.bookingDetailBreak);
		mBreak.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePickerDialog((EditText) v, true);
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

		Toolbar toolbar = (Toolbar) findViewById(R.id.booking_detail_toolbar);
		setSupportActionBar(toolbar);
		if (isModeNew()) {
			getSupportActionBar().setTitle(R.string.title_booking_new);
		}
		else {
			getSupportActionBar().setTitle(R.string.title_booking_edit);
		}
			
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


	private boolean isModeNew() {
		return theBooking.getId() == null;
	}

	private void prepareData() {
		if (theBooking != null) {
//			headLine.setText(DateUtil.getFormattedDayFull(theBooking.getFrom()));
			day.setText(DateUtil.getFormattedDayFull(theBooking.getFrom()));
			Calendar cDay = Calendar.getInstance();
			cDay.setTime(theBooking.getFrom());
			day.setTag(cDay);
			
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

			int breakHours = theBooking.getBreakHours() != null ? theBooking.getBreakHours() : 0;
			int breakMinutes = theBooking.getBreakMinutes() != null ? theBooking.getBreakMinutes() : 0;
			Calendar cBreak = Calendar.getInstance();
			cBreak.set(Calendar.HOUR_OF_DAY, breakHours);
			cBreak.set(Calendar.MINUTE, breakMinutes);
			mBreak.setText(DateUtil.getFormattedHM(breakHours + breakMinutes));
			mBreak.setTag(cBreak);

			/*
			mBreak.addTextChangedListener(new TextWatcher() {
				private final Pattern sPattern
						= Pattern.compile("[0-2]{1}[0-4]{1}:[0-6]{1}[0-9]{1}");

				private CharSequence mText;

				private boolean isValid(CharSequence s) {
					return sPattern.matcher(s).matches();
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after) {
					mText = isValid(s) ? s.toString() : "";
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (!isValid(s)) {
						mBreak.setText(mText);
					}
					mText = null;
				}
			});
			*/


			updateDuration();
			
			if (theBooking.getProjectId() != null) {
				prepareDataProject(theBooking.getProjectId());
				note.requestFocus();
			}
			else {
				projectView.requestFocus();
			}
			
			note.setText(theBooking.getNote() != null ? theBooking.getNote() : "");
		}
	}

	private void updateDuration() {
		Calendar cFrom = (Calendar) from.getTag();
		Calendar cTo = (Calendar) to.getTag();
		Calendar cBreak = (Calendar) mBreak.getTag();

		Integer breakTime = DateUtil.getBreakTime(cBreak);
		mDuration = DateUtil.getMinutes(cFrom.getTime(), cTo.getTime()) - breakTime;
		
		duration.setText(DateUtil.getFormattedHM(mDuration));
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
		Calendar cDay = (Calendar) this.day.getTag();
		Calendar cFrom = (Calendar) this.from.getTag();
		Calendar cTo = (Calendar) this.to.getTag();
		Calendar cBreak = (Calendar) this.mBreak.getTag();
		
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

		Integer breakTime = DateUtil.getBreakTime(cBreak);
		if (breakTime > mDuration) {
			Toast.makeText(this, "Pause darf nicht länger als Dauer sein.", Toast.LENGTH_SHORT).show();
			return;
		}

		Long projectId = (Long) this.projectView.getTag();
		if (projectId == null) {
			Toast.makeText(this, "Ein Projekt muss ausgewählt werden.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		this.theBooking.setFrom(cFromCombined.getTime());
		
		this.theBooking.setTo(cToCombined.getTime());

		Integer breakHours = breakTime / 60;
		this.theBooking.setBreakHours(breakHours);
		this.theBooking.setBreakMinutes((breakTime - breakHours * 60) % 60);
		
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
	
	private void showDatePickerDialog(EditText v) {
	    DialogFragment newFragment = new DatePickerFragment(v);
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog(final EditText v, boolean formatShort) {
	    DialogFragment newFragment = new TimePickerFragment(v, this, formatShort);
	    newFragment.show(getFragmentManager(), "timePicker");
	}

	private void showProjectSelectionDialog(View v) {
		ProjectSelectionDialogController dialogController = new ProjectSelectionDialogController(this, theBooking.getProjectId(), this);
		View dialogView = dialogController.buildView();

		ModalToolbarDialogFragment dialogFragment = new ModalToolbarDialogFragment();
		dialogFragment.init(dialogView, getResources().getString(R.string.title_project_selection), -1, -1, dialogController);
		dialogFragment.show(getFragmentManager(), "projectSelector");

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
