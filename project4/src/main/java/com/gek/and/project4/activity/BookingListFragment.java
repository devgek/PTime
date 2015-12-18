package com.gek.and.project4.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.listadapter.BookingListArrayAdapter;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.service.BookingService;
import com.gek.and.project4.types.PeriodType;
import com.gek.and.project4.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class BookingListFragment extends Fragment{
	private int periodPosition;
	private int projectPosition;
	TextView textViewSummaryTitle;
	TextView textViewSummary;
	ListView lvBookingList;
	
	public BookingListFragment(int periodPosition, int projectPosition) {
		super();
		this.periodPosition = periodPosition;
		this.projectPosition = projectPosition;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.booking_frame_content, container,  false);
		
		this.textViewSummaryTitle = (TextView) contentView.findViewById(R.id.booking_summary_title);
		this.textViewSummary = (TextView) contentView.findViewById(R.id.booking_summary_hm);
	    this.lvBookingList = (ListView) contentView.findViewById(R.id.booking_list_view);

	    textViewSummaryTitle.setText(getSummaryText(this.periodPosition));

		View rootView = (View) container.getRootView();
		ImageView addButton = (ImageView) rootView.findViewById(R.id.button_add_booking);
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BookingListActivity a = (BookingListActivity) getActivity();
				a.addBooking();
			}
		});


	    return contentView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		List<Booking> bookingList = getData();
		int minutes = getMinutes(bookingList);
		
		this.textViewSummary.setText(DateUtil.getFormattedHM(minutes));
		
		BookingListArrayAdapter bookingListAdapter = new BookingListArrayAdapter(R.layout.booking_row, getActivity());
		bookingListAdapter.addAll(bookingList);
		boolean showNote = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("setting_bookings_showNote", false);
		bookingListAdapter.setVisibilityLine3(showNote ? View.VISIBLE : View.GONE);

		this.lvBookingList.setAdapter(bookingListAdapter);
	} 

	private int getMinutes(List<Booking> bookingList) {
		int minutes = 0;
		
		for (Booking booking : bookingList) {
			if (booking.getTo() == null) {
				int runningMinutes = DateUtil.getMinutes(booking.getFrom(), Calendar.getInstance().getTime());
				booking.setMinutes(runningMinutes);
				Project4App.getApp(getActivity()).getSummary().setRunningNow(booking);
			}
			
			if (booking.getMinutes() != null) {
				minutes += booking.getMinutes();
			}
		}
		
		return minutes;
	}

	private String getSummaryText(int position) {
		Calendar initDate = Project4App.getApp(getActivity()).getSummary().getInitDate();
		switch (position) {
		case 0: return DateUtil.getFormattedDay(initDate);
		case 1: return DateUtil.getFormattedWeek(initDate);
		case 2: return DateUtil.getFormattedMonth(initDate);
		case 3: return DateUtil.getFormattedYear(initDate);
		default: return "";
		}
	}

	private Long getProjectIdFromPosition(int pos) {
		if (pos == 0) return -1L;
		
		ProjectCard card = Project4App.getApp(getActivity()).getProjectCardList().get(pos - 1);
		return card.getProject().getId();
	}

	private List<Booking> getData() {
		List<Booking> bookingList = Project4App.getApp(getActivity()).getLastBookingList();
		if (bookingList == null) {
			BookingService bookingService = Project4App.getApp(getActivity()).getBookingService();
			bookingList =  bookingService.getFiltered(PeriodType.fromInt(this.periodPosition), getProjectIdFromPosition(this.projectPosition));
			Project4App.getApp(getActivity()).setLastBookingList(bookingList);
		}
		
		return bookingList;
	}
}

	
	