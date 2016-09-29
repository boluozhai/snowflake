package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientFactory;

public class SmartClientImplementation {

	public static SmartClientFactory getClientFactory() {
		return new SmartClientFactoryImpl();
	}

}
