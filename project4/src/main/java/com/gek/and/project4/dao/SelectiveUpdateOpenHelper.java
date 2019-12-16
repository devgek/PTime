package com.gek.and.project4.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.gek.and.project4.util.L;


public class SelectiveUpdateOpenHelper extends DaoMaster.DevOpenHelper {

	public SelectiveUpdateOpenHelper(Context context, String name, CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.d("SelectiveUpdateOpenHelper", "Upgrading schema from version " + oldVersion + " to " + newVersion + " selective.");
        if (oldVersion < 7) {
            BookingTable.migrateTo7(db);
        }
        if (oldVersion < 8) {
            ProjectTable.migrateTo8(db);
        }
		if (oldVersion < 9) {
			ProjectTable.migrateTo9(db);
			BookingTable.migrateTo9(db);
		}
		if (oldVersion < 10) {
			ProjectTable.migrateTo10(db);
			BookingTable.migrateTo10(db);
		}
        else {
        	super.onUpgrade(db, oldVersion, newVersion);
        }
	}
	
	public static class BookingTable {
		public static void migrateTo7(SQLiteDatabase db) {
			db.execSQL("alter table " + BookingDao.TABLENAME + " add column NOTE text");
		}
		public static void migrateTo9(SQLiteDatabase db) {
			db.execSQL("alter table " + BookingDao.TABLENAME + " add column BREAK_HOURS integer default 0");
			db.execSQL("alter table " + BookingDao.TABLENAME + " add column BREAK_MINUTES integer default 0");
		}
		public static void migrateTo10(SQLiteDatabase db) {
			db.execSQL("alter table " + BookingDao.TABLENAME + " add column BILLABLE integer default 1");
		}
	}
	
	public static class ProjectTable {
		public static void migrateTo8(SQLiteDatabase db) {
			db.execSQL("alter table " + ProjectDao.TABLENAME + " add column ACTIVE integer default 1");
		}
		public static void migrateTo9(SQLiteDatabase db) {
			db.execSQL("alter table " + ProjectDao.TABLENAME + " add column DEFAULT_NOTE text");
		}
		public static void migrateTo10(SQLiteDatabase db) {
			db.execSQL("alter table " + ProjectDao.TABLENAME + " add column BILLABLE integer default 1");
		}
	}

}
