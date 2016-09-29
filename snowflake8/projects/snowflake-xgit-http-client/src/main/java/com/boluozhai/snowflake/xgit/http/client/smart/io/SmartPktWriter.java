package com.boluozhai.snowflake.xgit.http.client.smart.io;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;

public interface SmartPktWriter extends Closeable {

	void write(SmartPkt pkt) throws IOException;

}
