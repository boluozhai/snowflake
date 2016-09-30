package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientBuilder;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientFactory;

final class SmartClientFactoryImpl implements SmartClientFactory {

	public SmartClientFactoryImpl() {
	}

	@Override
	public SmartClientBuilder newBuilder() {
		return new SmartClientBuilderImpl();
	}

}
