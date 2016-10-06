package com.boluozhai.snowflake.installer.min.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.boluozhai.snowflake.util.HashTools;
import com.boluozhai.snowflake.util.IOTools;

public class HashToolsEx {

	public static String sha1(File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return HashTools.sha1string(in, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOTools.close(in);
		}
	}

}
