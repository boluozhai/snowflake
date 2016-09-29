package com.boluozhai.snowflake.xgit.http.client.smart;

import java.io.Closeable;
import java.io.IOException;

public interface SmartTx extends Closeable {

	void write(SmartPkt pkt) throws IOException;

	SmartRx openRx();

}
