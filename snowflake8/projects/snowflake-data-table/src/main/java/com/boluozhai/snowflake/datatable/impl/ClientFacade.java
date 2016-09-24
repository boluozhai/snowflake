package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;

import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.datatable.pojo.Model;

public class ClientFacade implements DataClient {

	private final DataClient inner;

	public ClientFacade(DataClient in) {
		this.inner = in;
	}

	public Transaction beginTransaction() {
		return inner.beginTransaction();
	}

	public <T extends Model> T insert(String name, T obj) {
		return inner.insert(name, obj);
	}

	public <T extends Model> T insertOrUpdate(String name, T obj) {
		return inner.insertOrUpdate(name, obj);
	}

	public <T extends Model> T update(T obj) {
		return inner.update(obj);
	}

	public <T extends Model> T get(String name, Class<T> type) {
		return inner.get(name, type);
	}

	public boolean delete(Model obj) {
		return inner.delete(obj);
	}

	public void close() throws IOException {
		inner.close();
	}

}
