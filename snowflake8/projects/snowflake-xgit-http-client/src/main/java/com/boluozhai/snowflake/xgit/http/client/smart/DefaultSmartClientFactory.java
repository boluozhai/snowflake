package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.http.client.smart.impl.SmartClientImplementation;

public class DefaultSmartClientFactory implements SmartClientFactory {

	private final SmartClientFactory inner;

	public DefaultSmartClientFactory() {
		this.inner = SmartClientImplementation.getClientFactory();
	}

	@Override
	public SmartClientBuilder newBuilder() {
		return inner.newBuilder();
	}

}
