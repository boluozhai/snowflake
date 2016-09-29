package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;

public interface InnerSmartConnection extends Closeable {

	void tx(SmartPkt pkt) throws IOException;

	SmartPkt rx() throws IOException;

}
