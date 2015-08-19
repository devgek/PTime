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
        if (newVersion == 7) {
            BookingTable.migrateTo7(db);
        }
        else
        if (newVersion == 8) {
            ProjectTable.migrateTo8(db);
        }
        else {
        	super.onUpgrade(db, oldVersion, newVersion);
        }
	}
	
	public static class BookingTable {
		public static void migrateTo7(SQLiteDatabase db) {
			db.execSQL("alter table " + BookingDao.TABLENAME + " add column NOTE text");
		}
	}
	
	public static class ProjectTable {
		public static void migrateTo8(SQLiteDatabase db) {
			db.execSQL("alter table " + ProjectDao.TABLENAME + " add column ACTIVE integer default 1");
		}
	}

}
