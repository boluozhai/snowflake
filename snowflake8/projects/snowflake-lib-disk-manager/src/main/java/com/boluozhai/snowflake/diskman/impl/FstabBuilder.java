package com.boluozhai.snowflake.diskman.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.boluozhai.snowflake.runtime.LineHandler;
import com.boluozhai.snowflake.runtime.SubProcess;

public class FstabBuilder implements LineHandler {

	private final String key_in_tab[];
	private final List<Properties> segments;
	private Properties current_segment;

	public FstabBuilder() {
		this.segments = new ArrayList<Properties>();
		this.key_in_tab = new String[4];
	}

	@Override
	public void onLine(SubProcess sp, final String text) {

		final int i = text.indexOf(':');
		if (i > 0) {

			final int cnt_tab = this.cnt_tab(text);
			final String s1 = text.substring(0, i).trim();
			final String s2 = text.substring(i + 1).trim();

			if (cnt_tab < key_in_tab.length) {
				key_in_tab[cnt_tab] = s1;
			}

			if (cnt_tab == 2) {
				this.on_key_value(s1, s2);
			} else if (cnt_tab == 4) {
				this.on_key_value(key_in_tab[2], s1, s2);
			}

		} else if (this.is_bar(text)) {
			this.flush_buffer();

		} else {
			// NOP
		}

	}

	private void on_key_value(String k1, String k2, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append(k1).append('.').append(k2);
		String key = this.make_key_regular(sb.toString());
		this.current_segment.setProperty(key, value);
	}

	private String make_key_regular(String s) {
		final int len = s.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			if (ch == ' ' || ch == '\t') {
				// NOP
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private void on_key_value(String key, String value) {
		key = this.make_key_regular(key);
		this.current_segment.setProperty(key, value);
	}

	private void flush_buffer() {
		for (int i = key_in_tab.length - 1; i >= 0; i--) {
			key_in_tab[i] = null;
		}
		Properties seg = this.current_segment;
		this.current_segment = new Properties();
		if (seg != null) {
			if (seg.size() > 0) {
				this.segments.add(seg);
			}
		}
	}

	private int cnt_tab(String text) {
		int cnt = 0;
		final int len = text.length();
		for (int i = 0; i < len; i++) {
			final char ch = text.charAt(i);
			if (ch == ' ') {
				cnt++;
			} else {
				break;
			}
		}
		return cnt;
	}

	private boolean is_bar(String text) {
		final int len = text.length();
		if (len < 20) {
			return false;
		}
		for (int i = len - 1; i >= 0; i--) {
			final char ch = text.charAt(i);
			if (ch == '=') {
				// OK
			} else {
				return false;
			}
		}
		return true;
	}

	public List<Properties> getResult() {
		return this.segments;
	}
}
