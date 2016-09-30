package com.boluozhai.snowflake.xgit.http.client.smart.io;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;

public class DefaultSmartPktHandler implements SmartPktHandler {

	private final boolean _accept;

	// public DefaultSmartPktHandler() {
	// this._accept = false;
	// }

	public DefaultSmartPktHandler(boolean accept) {
		this._accept = accept;
	}

	@Override
	public void onTx(SmartPkt pkt) {
	}

	@Override
	public void onRx(SmartPkt pkt) {
	}

	@Override
	public boolean accept(SmartPkt pkt) {
		return this._accept;
	}

}
