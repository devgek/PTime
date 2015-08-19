package com.gek.and.project4.util;

import java.io.File;

import android.app.Activity;

public class FileUtil {
	public static File getInternalFile(Activity parentActivity, String fileName) {
		File f = new File(parentActivity.getExternalFilesDir(null), fileName);
		return f;
	}
}
