package com.boluozhai.snowflake.xgit.http.client.smart.io;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;

public interface SmartPktReader extends Closeable {

	SmartPkt read() throws IOException;

}
