package com.boluozhai.snowflake.datatable.impl;

import com.boluozhai.snowflake.datatable.Transaction;

public class TransactionImpl implements Transaction {

	private final Transaction inner;

	public TransactionImpl(Transaction in) {
		this.inner = in;
	}

	@Override
	public void begin() {
		inner.begin();
	}

	@Override
	public void commit() {
		inner.commit();
	}

	@Override
	public void rollback() {
		inner.rollback();
	}

}
