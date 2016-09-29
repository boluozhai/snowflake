package com.boluozhai.snowflake.xgit.remotes;

public class Remote {

	private String name;
	private String url;
	private String fetch;
	private boolean xgit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFetch() {
		return fetch;
	}

	public void setFetch(String fetch) {
		this.fetch = fetch;
	}

	public boolean isXgit() {
		return xgit;
	}

	public void setXgit(boolean xgit) {
		this.xgit = xgit;
	}

}
