package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.datatable.DataLine;
import com.boluozhai.snowflake.datatable.Transaction;

public class ClientFacade implements DataClient {

	private final DataClient inner;

	public ClientFacade(DataClient in) {
		this.inner = in;
	}

	public Transaction beginTransaction() throws SnowflakeException {
		return inner.beginTransaction();
	}

	public DataLine line(String key, String type) {
		return inner.line(key, type);
	}

	public DataLine line(String key, Class<?> type) {
		return inner.line(key, type);
	}

	public String[] list(Class<?> type) {
		return inner.list(type);
	}

	public DataClientFactory getFactory() {
		return inner.getFactory();
	}

	public void close() throws IOException {
		inner.close();
	}

	public DataLine line(String key) {
		return inner.line(key);
	}

}
