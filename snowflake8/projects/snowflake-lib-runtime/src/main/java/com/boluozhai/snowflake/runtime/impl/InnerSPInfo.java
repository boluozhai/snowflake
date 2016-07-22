package com.boluozhai.snowflake.runtime.impl;

import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.runtime.ErrorHandler;
import com.boluozhai.snowflake.runtime.LineHandler;

final class InnerSPInfo {

	public String command;
	public SnowContext context;
	public LineHandler h_output;
	public ErrorHandler h_error;

	public InnerSPInfo() {
	}

	public InnerSPInfo(InnerSPInfo src) {
		this.command = src.command;
		this.context = src.context;
		this.h_error = src.h_error;
		this.h_output = src.h_output;
	}

	public InnerSPInfo make_copy() {
		return new InnerSPInfo(this);
	}

	public static String[] toStringArray(String cmd) {
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		if (cmd != null) {
			char[] chs = cmd.toCharArray();
			for (char ch : chs) {
				if (ch == 0x0d || ch == 0x0a) {
					inner_flush_string_buffer(sb, list);
				} else {
					sb.append(ch);
				}
			}
			inner_flush_string_buffer(sb, list);
		}
		return list.toArray(new String[list.size()]);
	}

	private static void inner_flush_string_buffer(StringBuilder sb,
			List<String> list) {
		if (sb.length() > 0) {
			String s = sb.toString().trim();
			sb.setLength(0);
			if (s.length() > 0) {
				list.add(s);
			}
		}
	}

}
