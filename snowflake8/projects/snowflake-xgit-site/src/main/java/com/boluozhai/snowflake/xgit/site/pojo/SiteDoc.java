package com.boluozhai.snowflake.xgit.site.pojo;

public class SiteDoc {

	private String type;

	public SiteDoc() {
		this.type = this.getClass().getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
