package com.boluozhai.snowflake.installer.min.pojo;

public class WebResponse extends DocModel {

	private MetaFile meta;
	private String error;

	public MetaFile getMeta() {
		return meta;
	}

	public void setMeta(MetaFile meta) {
		this.meta = meta;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
