package com.gek.and.project4.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gek.and.project4.dao.BookingDao;
import com.gek.and.project4.dao.BookingDao.Properties;
import com.gek.and.project4.dao.DaoSession;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.types.PeriodType;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.L;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

public class BookingService {
	private static final String TAG = "Project4:BookingService::";
	private BookingDao bookingDao;
	private final DaoSession daoSession;
	
	public BookingService(DaoSession daoSession) {
		this.daoSession = daoSession;
		this.bookingDao = daoSession.getBookingDao();
	}
	
	public void updateBooking(Booking booking) {
		booking.setMinutes(DateUtil.getMinutes(booking.getFrom(), booking.getTo()));
		if (booking.getId() == null) {
			this.bookingDao.insert(booking);
		}
		else {
			this.bookingDao.update(booking);
		}
	}
	
	public List<Booking> getFiltered(PeriodType periodType, Long projectId) {
		List<Booking> bookingList;
		
		switch (periodType) {
			case TODAY: bookingList = getToday();break;
			case WEEK: bookingList = getThisWeek();break;
			case MONTH: bookingList = getThisMonth();break;
			case YEAR: bookingList = getThisYear();break;
			case PRIOR_YEAR: bookingList = getPriorYear();break;
			case PRIOR_MONTH: bookingList = getPriorMonth();break;
			default: bookingList = new ArrayList<Booking>();
		}
		
		if (projectId != null && projectId > -1) {
			bookingList = filterProject(bookingList, projectId);
		}
		return bookingList;
	}
	
	private List<Booking> filterProject(List<Booking> bookingList, Long projectId) {
		List<Booking> filteredList = new ArrayList<Booking>();
		
		for (Booking booking : bookingList) {
			if (booking.getProjectId().equals(projectId)) {
				filteredList.add(booking);
			}
		}
		
		return filteredList;
	}

	public List<Booking> getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, 0);
		
//		L.d(TAG, "compare date today: " + cal.toString());
//		L.d(TAG, "compare date today formatted: " + DateUtil.getFormattedDateTime(cal.getTime()));

		return getGreaterEqual(cal);
	}
	
	public List<Booking> getThisWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
//		L.d(TAG, "compare date week formatted: " + DateUtil.getFormattedDateTime(cal.getTime()));
		return getGreaterEqual(cal);
	}
	
	public List<Booking> getThisMonth() {
		Calendar calFrom = Calendar.getInstance();
		int year = calFrom.get(Calendar.YEAR);
		int month = calFrom.get(Calendar.MONTH);
		calFrom.clear();
		calFrom.set(Calendar.YEAR, year);
		calFrom.set(Calendar.MONTH, month);
		
		Calendar calTo = Calendar.getInstance();
		calTo.clear();
		calTo.set(Calendar.YEAR, year);
		calTo.set(Calendar.MONTH, month + 1);
		
		return getBeetween(calFrom, calTo);
	}
	
	public List<Booking> getThisYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		return getGreaterEqual(cal);
	}

	public List<Booking> getPriorYear() {
		Calendar calFrom = Calendar.getInstance();
		int year = calFrom.get(Calendar.YEAR);
		calFrom.clear();
		calFrom.set(Calendar.YEAR, year - 1);
		calFrom.set(Calendar.MONTH, 0);
		calFrom.set(Calendar.DAY_OF_MONTH, 1);

		Calendar calTo = Calendar.getInstance();
		calTo.clear();
		calTo.set(Calendar.YEAR, year - 1);
		calTo.set(Calendar.MONTH, 11);
		calTo.set(Calendar.DAY_OF_MONTH, 31);

		return getBeetween(calFrom, calTo);
	}

	public List<Booking> getPriorMonth() {
		Calendar calTo = Calendar.getInstance();
		int year = calTo.get(Calendar.YEAR);
		int month = calTo.get(Calendar.MONTH);
		calTo.clear();
		calTo.set(Calendar.YEAR, year);
		calTo.set(Calendar.MONTH, month);
		calTo.set(Calendar.DAY_OF_MONTH, 1);

		Calendar calFrom = Calendar.getInstance();
		calFrom.clear();
		if (month == 0) {
			calFrom.set(Calendar.YEAR, year - 1);
			calFrom.set(Calendar.MONTH, 11);
			calFrom.set(Calendar.DAY_OF_MONTH, 1);
		}
		else {
			calFrom.set(Calendar.YEAR, year);
			calFrom.set(Calendar.MONTH, month - 1);
			calFrom.set(Calendar.DAY_OF_MONTH, 1);
		}

		return getBeetween(calFrom, calTo);
	}

	private List<Booking> getGreaterEqual(Calendar compareDate) {
		QueryBuilder<Booking> qb = this.bookingDao.queryBuilder();
//		qb.where(Properties.From.ge(cal.getTime())).where(Properties.To.isNotNull()).orderAsc(Properties.From);
		qb.where(Properties.From.ge(compareDate.getTime())).orderDesc(Properties.From);
		return qb.list();
	}
	
	private List<Booking> getBeetween(Calendar fromDate, Calendar toDate) {
		QueryBuilder<Booking> qb = this.bookingDao.queryBuilder();
//		qb.where(Properties.From.ge(cal.getTime())).where(Properties.To.isNotNull()).orderAsc(Properties.From);
		qb.where(Properties.From.ge(fromDate.getTime())).where(Properties.From.lt(toDate.getTime())).orderDesc(Properties.From);
		return qb.list();
	}
	
	public Booking bookStart(long projectId) {
		Project project = this.daoSession.getProjectDao().load(projectId);
		Booking start = new Booking();
		start.setProjectId(projectId);
		start.setNote(project.getDefaultNote());
		start.setBreakHours(0);
		start.setBreakMinutes(0);
		start.setMinutes(0);
		start.setFrom(DateUtil.getNowSmoothed());
		long bookingId = this.bookingDao.insert(start);
		if (bookingId > 0) {
			L.d(TAG, "Project started at:" + start.getFrom());
			return this.bookingDao.load(bookingId);
		}
		else {
			return null;
		}
	}

	public Booking bookStop(Booking lastOpenBooking) {
		Calendar cStop = Calendar.getInstance();
		Calendar cStart = Calendar.getInstance();
		cStart.setTime(lastOpenBooking.getFrom());
		if (cStart.get(Calendar.DAY_OF_YEAR) == cStop.get(Calendar.DAY_OF_YEAR)) {
			lastOpenBooking.setTo(DateUtil.getSmoothed(cStop));
			updateBooking(lastOpenBooking);
			L.d(TAG, "Project stopped at:" + lastOpenBooking.getTo() + " with minutes: " + lastOpenBooking.getMinutes());
			return lastOpenBooking;
		}
		else {
			return splitStopBooking(lastOpenBooking, cStart, cStop);
		}
	}

	public boolean deleteBooking(Booking booking) {
		boolean ok = true;

		try {
			this.bookingDao.delete(booking);
		}
		catch(Exception e) {
			ok = false;
		}

		return ok;
	}

	public boolean importBookings(List<Booking> bookings) {
		SQLiteDatabase db = this.daoSession.getDatabase();
		boolean ok = true;

		db.beginTransaction();
		try {
			bookings.forEach(booking -> deleteSameDay(booking));
			this.daoSession.clear();
			bookings.forEach(booking -> updateBooking(booking));
			db.setTransactionSuccessful();
		}
		catch(Exception e) {
			ok = false;
		}
		finally {
			db.endTransaction();
		}

		this.daoSession.clear();
		return ok;
	}

	private Booking splitStopBooking(Booking lastOpenBooking, Calendar cStart,	Calendar cStop) {
		int startDay = cStart.get(Calendar.DAY_OF_YEAR);
		int stopDay = cStop.get(Calendar.DAY_OF_YEAR);
		
		//startDay
		Calendar cFrom;
		Calendar cTo = Calendar.getInstance();
		cTo.setTime(cStart.getTime());
		cTo.set(Calendar.HOUR_OF_DAY, 23);
		cTo.set(Calendar.MINUTE, 59);
		cTo.set(Calendar.SECOND, 59);

		lastOpenBooking.setTo(cTo.getTime());
		lastOpenBooking.setMinutes(DateUtil.getMinutes(lastOpenBooking.getFrom(), lastOpenBooking.getTo()));
		
		bookingDao.update(lastOpenBooking);
		
		//if there is more than one day left
		for (int iDay = startDay + 1; iDay < stopDay; iDay++) {
			cFrom = Calendar.getInstance();
			cFrom.set(Calendar.DAY_OF_YEAR, iDay);
			cFrom.set(Calendar.HOUR_OF_DAY, 0);
			cFrom.set(Calendar.MINUTE, 0);
			cFrom.set(Calendar.SECOND, 0);
			
			cTo = Calendar.getInstance();
			cTo.set(Calendar.DAY_OF_YEAR, iDay);
			cTo.set(Calendar.HOUR_OF_DAY, 23);
			cTo.set(Calendar.MINUTE, 59);
			cTo.set(Calendar.SECOND, 59);
			
			Booking splitBooking = new Booking();
			splitBooking.setProjectId(lastOpenBooking.getProjectId());
			splitBooking.setNote(lastOpenBooking.getNote());
			splitBooking.setFrom(cFrom.getTime());
			splitBooking.setTo(cTo.getTime());
			splitBooking.setMinutes(DateUtil.getMinutes(cFrom.getTime(), cTo.getTime()));
			bookingDao.insert(splitBooking);
		}
		
		//stopDay
		cFrom = Calendar.getInstance();
		cFrom.set(Calendar.DAY_OF_YEAR, stopDay);
		cFrom.set(Calendar.HOUR_OF_DAY, 0);
		cFrom.set(Calendar.MINUTE, 0);
		cFrom.set(Calendar.SECOND, 0);
		
		cTo = Calendar.getInstance();
		cTo.setTime(cStop.getTime());

		Booking stopBooking = new Booking();
		stopBooking.setProjectId(lastOpenBooking.getProjectId());
		stopBooking.setNote(lastOpenBooking.getNote());
		stopBooking.setFrom(cFrom.getTime());
		stopBooking.setTo(cTo.getTime());
		stopBooking.setMinutes(DateUtil.getMinutes(cFrom.getTime(), cTo.getTime()));
		Long id = bookingDao.insert(stopBooking);

		stopBooking.setId(id);
		return stopBooking;
	}


	private void deleteSameDay(Booking booking) {
		SQLiteDatabase db = this.daoSession.getDatabase();
		BookingDao bookingDao = this.daoSession.getBookingDao();

		String fromDay = DateUtil.getFormattedDate(booking.getFrom());
		Date nextDayDate = DateUtil.getNextDay(booking.getFrom());
		String nextDay = DateUtil.getFormattedDate(nextDayDate);
		Date fromDayBegin = DateUtil.getDayBegin(booking.getFrom());
		Date fromDayEnd = DateUtil.getDayEnd(booking.getFrom());

//		String sql = "delete from " + bookingDao.getTablename() + " where date(\"" + Properties.From.columnName + "\") >= date('" + fromDay + "') and date(\""
//				+ Properties.From.columnName + "\") < date('" + nextDay + "')";
//		String sql = "delete from booking where \"from\" >= '2018-02-01 00:00:00' and \"from\" < '2018-02-02 00:00:00'";
//		String sql = "delete from booking where project_id = " + booking.getProjectId(); // + " and date(\"from\") = '" + fromDay + "'";
//		String sql = "delete from booking where project_id = " + booking.getProjectId() + " and strftime(\"%Y-%m-%d\", \"from\") = '" + fromDay + "'";
//		db.execSQL(sql);
//		int rowsDeleted = db.delete(bookingDao.getTablename(), "project_id=? and strftime(\"%Y-%m-%d\", \"from\")=?", new String[]{String.valueOf(booking.getProjectId()), fromDay});
//		if (rowsDeleted > 0) {
//			System.out.println(rowsDeleted);
//		}
//		int rowsDeleted = db.delete(bookingDao.getTablename(), "project_id=? and strftime('%Y-%m-%d', 'from')=?", new String[]{String.valueOf(booking.getProjectId()), fromDay});
//		int rowsDeleted = db.delete(bookingDao.getTablename(), "project_id=?", new String[]{String.valueOf(booking.getProjectId())});
//		String sql = "select project_id, date('FROM') as von, note  from booking where project_id = " + booking.getProjectId();
//		Cursor cursor = db.rawQuery(sql, null);
//		while (cursor != null && cursor.moveToNext()) {
//			String note = cursor.getString(cursor.getColumnIndex("NOTE"));
//			String theDate = cursor.getString(cursor.getColumnIndex("von"));
//			Long pid = cursor.getLong(cursor.getColumnIndex("PROJECT_ID"));
//			String x = "";
//		}
		QueryBuilder builder = bookingDao.queryBuilder();
		builder.where(Properties.From.ge(fromDayBegin)).where(Properties.From.le(fromDayEnd));
		DeleteQuery deleteQuery = builder.buildDelete();
		deleteQuery.executeDeleteWithoutDetachingEntities();
	}


}
