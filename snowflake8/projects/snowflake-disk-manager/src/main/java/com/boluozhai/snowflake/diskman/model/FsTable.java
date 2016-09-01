package com.boluozhai.snowflake.diskman.model;

import java.util.Map;

public class FsTable {

	private Map<String, FsItem> items;
	private Map<String, FsItem> alias;

	public Map<String, FsItem> getItems() {
		return items;
	}

	public void setItems(Map<String, FsItem> items) {
		this.items = items;
	}

	public Map<String, FsItem> getAlias() {
		return alias;
	}

	public void setAlias(Map<String, FsItem> alias) {
		this.alias = alias;
	}

}
