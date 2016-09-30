package com.boluozhai.snowflake.xgit.http.client.smart;

import java.io.IOException;

public interface SmartClient extends SmartClientConfigGetter {

	SmartTx openTx() throws IOException;

	SmartTx openTx(String resource, String service) throws IOException;

}
