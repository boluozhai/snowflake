package com.boluozhai.snowflake.datatable.pojo;

public class TestingB {

	private int hash;

	public TestingB() {
		this.hash = this.hashCode();
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

}
