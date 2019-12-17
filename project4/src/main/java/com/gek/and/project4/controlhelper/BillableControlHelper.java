package com.gek.and.project4.controlhelper;


import android.widget.CheckedTextView;

import com.gek.and.project4.R;

public class BillableControlHelper{
	public static void setText(CheckedTextView billable) {
		if (billable.isChecked()) {
			billable.setText(R.string.billable_edit_text_on);
		}
		else {
			billable.setText(R.string.billable_edit_text_off);
		}
	}
}
