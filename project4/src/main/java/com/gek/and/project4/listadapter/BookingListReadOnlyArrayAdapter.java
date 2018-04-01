package com.gek.and.project4.listadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gek.and.project4.R;
import com.gek.and.project4.activity.BookingDetailActivity;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.util.BookingUtil;
import com.gek.and.project4.util.ColorUtil;
import com.gek.and.project4.util.DateUtil;

public class BookingListReadOnlyArrayAdapter extends BookingListArrayAdapter {

	public BookingListReadOnlyArrayAdapter(int resource, Activity parentActivity) {
		super(resource, parentActivity);
	}

	protected void handleClick(View row) {
		//do nothing here
	}

}
