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

public class BookingListArrayAdapter extends ArrayAdapter<Booking> {
	static class BookingViewHolder {
		ImageView colorBar;
		TextView line1;
		TextView line2Left;
		TextView line2Right;
		TextView line3;
		Booking booking;
	}
	
	private Activity parentActivity;
	private int visibilyLine3 = View.VISIBLE;

	public BookingListArrayAdapter(int resource, Activity parentActivity) {
		super(parentActivity.getApplicationContext(), resource);
		this.parentActivity = parentActivity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Booking booking = getItem(position);

		View row = convertView;
		BookingViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.booking_row, parent, false);
			
			viewHolder = new BookingViewHolder();
			viewHolder.colorBar = (ImageView) row.findViewById(R.id.booking_row_color_bar);
			viewHolder.line1 = (TextView) row.findViewById(R.id.booking_row_line1);
			viewHolder.line2Left = (TextView) row.findViewById(R.id.booking_row_line2_left);
			viewHolder.line2Right = (TextView) row.findViewById(R.id.booking_row_line2_right);
			viewHolder.line3 = (TextView) row.findViewById(R.id.booking_row_line3);
			
			handleClick(row);
			
			row.setTag(viewHolder);
		} else {
			viewHolder = (BookingViewHolder) row.getTag();
		}
		
		viewHolder.colorBar.setBackgroundColor(ColorUtil.getInt(getColor(booking)));
		viewHolder.line1.setText(getLine1(booking));
		viewHolder.line2Left.setText(getLine2Left(booking));
		viewHolder.line2Right.setText(getLine2Right(booking));
		viewHolder.line3.setText(getLine3(booking));
		viewHolder.line3.setVisibility(this.visibilyLine3);
		viewHolder.booking = booking;

		return row;
	}
	
	public void setVisibilityLine3(int visibility) {
		this.visibilyLine3 = visibility;
	}

	private CharSequence getLine3(Booking booking) {
		return booking.getNote() != null ? booking.getNote() : "";
	}

	private String getLine2Right(Booking booking) {
		return booking.getMinutes() != null ? DateUtil.getFormattedHM(BookingUtil.getDuration(booking)) : "";
	}

	public String getLine2Left(Booking booking) {
		StringBuffer buf = new StringBuffer();
		buf.append(DateUtil.getFormattedDate(booking.getFrom()));
		buf.append("   ");
		buf.append(DateUtil.getFormattedHM(booking.getFrom()));
		buf.append("   ");
		buf.append(booking.getTo() != null ? DateUtil.getFormattedHM(booking.getTo()) : "läuft ...");
		int breakTotal = BookingUtil.getBreakTotal(booking);
		if (breakTotal > 0) {
			buf.append("      ");
			buf.append(DateUtil.getFormattedHM(breakTotal));
		}
		
		return buf.toString();
	}

	private String getLine1(Booking booking) {
		ProjectService projectService = Project4App.getApp(parentActivity).getProjectService();
		Project p = projectService.getProject(booking.getProjectId());
		if (p != null) {
			return p.getCompany() + " / " + p.getTitle();
		}
		else {
			return "";
		}
	}
	
	private String getColor(Booking booking) {
		ProjectService projectService = Project4App.getApp(parentActivity).getProjectService();
		Project p = projectService.getProject(booking.getProjectId());
		if (p != null) {
			return p.getColor();
		}
		else {
			return ColorUtil.getHex(R.color.no_project_color);
		}
	}

	protected void handleClick(View row) {
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BookingViewHolder holder = (BookingViewHolder)v.getTag();
				Project4App.getApp(parentActivity).setEditBooking(holder.booking);
				
				Intent intent = new Intent(parentActivity, BookingDetailActivity.class);
				parentActivity.startActivityForResult(intent, 3000);
			}
		});
	}

}
