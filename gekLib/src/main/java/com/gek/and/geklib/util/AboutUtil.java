package com.gek.and.geklib.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

public class AboutUtil {
	public static String readContentFromAsset(AssetManager assetManager, String assetFileName) {
		InputStream is = null;
		try {
			is = assetManager.open(assetFileName);
			
			return FileUtil.readTextContent(is);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
