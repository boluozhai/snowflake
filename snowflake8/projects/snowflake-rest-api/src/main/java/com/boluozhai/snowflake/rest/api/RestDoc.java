package com.boluozhai.snowflake.rest.api;

public class RestDoc {

	private String type;

	public RestDoc() {
		this.type = this.getClass().getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
