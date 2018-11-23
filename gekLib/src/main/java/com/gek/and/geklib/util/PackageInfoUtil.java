package com.gek.and.geklib.util;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageInfoUtil {
	public static String getVersionName(Application app) {
		PackageInfo info;
		try {
			info = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
			return info.versionName + " [" + info.versionCode + "]" ;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
}
