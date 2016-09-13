package com.boluozhai.snowflake.xgit.http.server;

public class GitHttpInfoHolder {

	private GitHttpInfo info;
	private String type;

	public GitHttpInfoHolder() {
		this.type = this.getClass().getName();
	}

	public GitHttpInfo getInfo() {
		return info;
	}

	public void setInfo(GitHttpInfo info) {
		this.info = info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
