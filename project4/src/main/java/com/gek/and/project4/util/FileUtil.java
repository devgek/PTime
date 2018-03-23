package com.gek.and.project4.util;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileUtil {
	public static File getInternalFile(Activity parentActivity, String fileName) {
		File f = new File(parentActivity.getExternalFilesDir(null), fileName);
		return f;
	}

	public static Properties loadProperties(File propertiesFile) throws IOException {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(propertiesFile);
		props.load(fis);

		return props;
	}
}
