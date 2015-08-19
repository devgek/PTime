package com.gek.and.project4.util;

import android.util.Log;

public class L {
	private static boolean debugOn = android.os.Debug.isDebuggerConnected();
	
	public static void d(String tag, String message) {
		if (debugOn) {
			Log.d(tag, message);
		}
	}
}
