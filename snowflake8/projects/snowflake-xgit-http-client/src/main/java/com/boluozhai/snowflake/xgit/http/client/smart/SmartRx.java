package com.boluozhai.snowflake.xgit.http.client.smart;

import java.io.Closeable;
import java.io.IOException;

public interface SmartRx extends Closeable {

	SmartPkt read() throws IOException;

}
