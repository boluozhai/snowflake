package com.boluozhai.snowflake.datatable.pojo;

public class TestingA implements Model {

	private long time;

	public TestingA() {
		this.time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
