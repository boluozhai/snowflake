package com.boluozhai.snowflake.xgit.http.client.smart;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktWriter;

public interface SmartTx extends SmartPktWriter {

	SmartRx openRx() throws IOException;

}
