package com.boluozhai.snowflake.datatable.impl;

import com.boluozhai.snowflake.datatable.DataLine;

public class LineFacade implements DataLine {

	private final DataLine inner;

	public LineFacade(DataLine in) {
		this.inner = in;
	}

	public String getKey() {
		return inner.getKey();
	}

	public <T> T insert(T obj) {
		return inner.insert(obj);
	}

	public <T> T update(T obj) {
		return inner.update(obj);
	}

	public boolean delete() {
		return inner.delete();
	}

	public boolean exists() {
		return inner.exists();
	}

	public <T> T get(Class<T> type) {
		return inner.get(type);
	}

	public Class<?> getTypeClass() {
		return inner.getTypeClass();
	}

	public String getType() {
		return inner.getType();
	}

	public String getName() {
		return inner.getName();
	}

}
