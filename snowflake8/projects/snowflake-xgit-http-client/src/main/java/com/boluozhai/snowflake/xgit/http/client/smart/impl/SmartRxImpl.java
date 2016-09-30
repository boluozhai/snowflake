package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartRx;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktReader;

final class SmartRxImpl implements SmartRx {

	// private final InnerSmartCore core;
	private final SmartPktReader rx;

	public SmartRxImpl(InnerSmartCore core) throws IOException {
		// this.core = core;
		this.rx = core.open_rx();
	}

	@Override
	public SmartPkt read() throws IOException {
		return rx.read();
	}

	@Override
	public void close() throws IOException {
		rx.close();
	}

}
