package com.boluozhai.snowflake.datatable.impl;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.Transaction;

public class TransactionFacade implements Transaction {

	private final Transaction inner;

	public TransactionFacade(Transaction in) {
		this.inner = in;
	}

	public void begin() throws SnowflakeException {
		inner.begin();
	}

	public void commit() throws SnowflakeException {
		inner.commit();
	}

	public void rollback() throws SnowflakeException {
		inner.rollback();
	}

}
