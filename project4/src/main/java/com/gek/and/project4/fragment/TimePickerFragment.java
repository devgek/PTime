package com.gek.and.project4.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import com.gek.and.project4.util.DateUtil;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	private EditText editText;
	private OnTimeSetListener timeSetListener;
	private boolean formatShort;
	
	public TimePickerFragment(EditText editView, OnTimeSetListener listener) {
		this(editView, listener, false);
	}

	public TimePickerFragment(EditText editView, OnTimeSetListener listener, boolean formatShort) {
		this.editText = editView;
		this.timeSetListener = listener;
		this.formatShort = formatShort;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		Calendar c = (Calendar) editText.getTag();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		Dialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		return dialog;
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
		Calendar c = (Calendar) editText.getTag();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);

		if (formatShort) {
			Integer minutes = DateUtil.getMinutes(c);
			editText.setText(DateUtil.getFormattedHM(minutes));
		}
		else {
			editText.setText(DateUtil.getFormattedTime(c.getTime()));
		}

		timeSetListener.onTimeSet();
	}

	public interface OnTimeSetListener {
		public void onTimeSet();
	}
}