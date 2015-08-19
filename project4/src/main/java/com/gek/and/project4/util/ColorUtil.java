package com.gek.and.project4.util;

import android.graphics.Color;

public class ColorUtil {
	public static String getHex(int color) {
		return String.format("#%06X", 0xFFFFFF & color);
	}
	
	public static int getInt(String hex) {
		return Color.parseColor(hex);
	}
}
