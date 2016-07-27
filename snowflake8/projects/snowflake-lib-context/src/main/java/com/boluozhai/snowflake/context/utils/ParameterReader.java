package com.boluozhai.snowflake.context.utils;

import com.boluozhai.snowflake.context.SnowParameters;

public class ParameterReader {

	private final SnowParameters p;

	public ParameterReader(SnowParameters p) {
		this.p = p;
	}

	public static String key4index(int index) {
		return String.format("%d", index);
	}

	public String get(int index) {
		String key = key4index(index);
		return p.getParameter(key);
	}

	public String get(int index, Object defaultValue) {
		String key = key4index(index);
		return p.getParameter(key, defaultValue);
	}

}
