package com.boluozhai.snowflake.xgit.pojo;

import com.boluozhai.snowflake.xgit.ObjectId;

public class PlainId {

	private String value;

	public PlainId() {
	}

	public PlainId(String v) {
		this.value = v;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ObjectId convert(PlainId id) {
		String s = id.getValue();
		return ObjectId.Factory.create(s);
	}

	public static PlainId convert(ObjectId id) {
		PlainId pid = new PlainId();
		pid.setValue(id.toString());
		return pid;
	}

}
