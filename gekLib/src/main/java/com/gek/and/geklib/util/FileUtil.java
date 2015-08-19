package com.gek.and.geklib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil {
	public static String readTextContent(InputStream is) throws IOException {
		Reader reader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(reader);
		
		StringBuffer buffer = new StringBuffer();
		String line;
        while((line = br.readLine()) != null) {
             buffer.append(line);
        }
		
		br.close();

		return buffer.toString();
	}
}
