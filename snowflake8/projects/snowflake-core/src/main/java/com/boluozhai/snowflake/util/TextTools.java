package com.boluozhai.snowflake.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class TextTools {

	public static final String enc_utf8 = "utf-8";
	public static final String enc_default = enc_utf8;

	public static void save(String text, File file) throws IOException {
		save(text, file, false);
	}

	public static void save(String text, File file, boolean mkdirs)
			throws IOException {
		OutputStream os = null;
		try {
			if (mkdirs) {
				File dir = file.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
			os = new FileOutputStream(file);
			save(text, os);
		} finally {
			IOTools.close(os);
		}
	}

	public static void save(String text, Writer out) throws IOException {
		out.write(text);
	}

	public static void save(String text, OutputStream out) throws IOException {
		Writer wtr = null;
		try {
			wtr = new OutputStreamWriter(out, enc_default);
			save(text, wtr);
		} finally {
			IOTools.close(wtr);
		}
	}

	public static String load(File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return load(in);
		} finally {
			IOTools.close(in);
		}
	}

	public static String load(Reader in) throws IOException {
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[1024];
		for (;;) {
			final int cc = in.read(buffer);
			if (cc < 0) {
				break;
			} else if (cc == 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				sb.append(buffer, 0, cc);
			}
		}
		return sb.toString();
	}

	public static String load(InputStream in) throws IOException {
		Reader reader = null;
		try {
			reader = new InputStreamReader(in, enc_default);
			return load(reader);
		} finally {
			IOTools.close(reader);
		}
	}

	public static String[] splitToLines(String text) {
		if (text == null) {
			String[] empty = {};
			return empty;
		}
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		char[] chs = text.toCharArray();
		for (char ch : chs) {
			if (ch == 0x0a || ch == 0x0d) {
				if (sb.length() > 0) {
					list.add(sb.toString());
					sb.setLength(0);
				}
			} else {
				sb.append(ch);
			}
		}
		if (sb.length() > 0) {
			list.add(sb.toString());
		}
		return list.toArray(new String[list.size()]);
	}

}
