package com.gek.and.geklib.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.ViewConfiguration;

public class WorkaroundActionOverflow {
	public static void execute(Activity activity) {
		//workaround for galaxy s2 - bug, not showing 3 dots overflow menu
		try {
	        ViewConfiguration config = ViewConfiguration.get(activity);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }
		
	}
}
