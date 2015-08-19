package com.gek.and.project4.util;

import android.content.Context;
import android.content.Intent;

public class ActivityUtil {
	public static void startActivity(Context context, Class activityClass) {
    	Intent intent = new Intent(context, activityClass);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
