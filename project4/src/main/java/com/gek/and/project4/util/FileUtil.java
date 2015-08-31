package com.gek.and.project4.util;

import android.app.Activity;

import java.io.File;

public class FileUtil {
	public static File getInternalFile(Activity parentActivity, String fileName) {
		File f = new File(parentActivity.getExternalFilesDir(null), fileName);
		return f;
	}
}
