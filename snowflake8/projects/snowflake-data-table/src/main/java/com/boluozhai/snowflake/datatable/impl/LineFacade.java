package com.boluozhai.snowflake.datatable.impl;

import com.boluozhai.snowflake.datatable.DataLine;
import com.boluozhai.snowflake.xgit.ObjectId;

public class LineFacade implements DataLine {

	private final DataLine inner;

	public LineFacade(DataLine in) {
		this.inner = in;
	}

	public String getUser() {
		return inner.getUser();
	}

	public String getHost() {
		return inner.getHost();
	}

	public ObjectId getId() {
		return inner.getId();
	}

	public <T> T insert(T obj) {
		return inner.insert(obj);
	}

	public <T> T insertOrUpdate(T obj) {
		return inner.insertOrUpdate(obj);
	}

	public boolean exists(Class<?> type) {
		return inner.exists(type);
	}

	public <T> T update(T obj) {
		return inner.update(obj);
	}

	public boolean delete(Object obj) {
		return inner.delete(obj);
	}

	public <T> T get(Class<T> type) {
		return inner.get(type);
	}

}
