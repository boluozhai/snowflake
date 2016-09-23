package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;

import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataLine;

public class ClientFacade implements DataClient {

	private final DataClient inner;

	public ClientFacade(DataClient in) {
		this.inner = in;
	}

	public DataLine line(String email) {
		return inner.line(email);
	}

	public DataLine line(String host, String user) {
		return inner.line(host, user);
	}

	public String[] list(Class<?> type) {
		return inner.list(type);
	}

	public void close() throws IOException {
		inner.close();
	}

}
