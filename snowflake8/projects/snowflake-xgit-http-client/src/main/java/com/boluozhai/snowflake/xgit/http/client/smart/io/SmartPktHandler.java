package com.boluozhai.snowflake.xgit.http.client.smart.io;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;

public interface SmartPktHandler {

	void onTx(SmartPkt pkt);

	void onRx(SmartPkt pkt);

}
