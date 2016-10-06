package com.boluozhai.snowflake.installer.min.pojo;

public class DocModel {

	private String type;

	public DocModel() {
		this.type = this.getClass().getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
