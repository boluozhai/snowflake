package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartRx;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartTx;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktWriter;

final class SmartTxImpl implements SmartTx {

	private final InnerSmartCore core;
	private final SmartPktWriter tx;

	public SmartTxImpl(InnerSmartCore core) throws IOException {
		this.core = core;
		this.tx = core.open_tx();
	}

	@Override
	public void write(SmartPkt pkt) throws IOException {
		tx.write(pkt);
	}

	@Override
	public void close() throws IOException {
		tx.close();
	}

	@Override
	public SmartRx openRx() throws IOException {
		return new SmartRxImpl(this.core);
	}

}
