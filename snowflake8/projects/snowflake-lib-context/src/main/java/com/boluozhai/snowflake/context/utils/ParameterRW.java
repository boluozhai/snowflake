package com.boluozhai.snowflake.context.utils;

import com.boluozhai.snowflake.context.MutableParameters;

public class ParameterRW extends ParameterReader {

	private final MutableParameters mp;

	public ParameterRW(MutableParameters p) {
		super(p);
		this.mp = p;
	}

	public void set(int index, String value) {
		String key = key4index(index);
		mp.setParameter(key, value);
	}

}
