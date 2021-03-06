package com.boluozhai.snowflake.discovery.udp;

import java.io.IOException;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface Hub {

	PublicHandler getPublicHandler();

	PrivateHandler getPrivateHandler();

	int getPort();

	HubRuntime createRuntime();

	Endpoint openEndpoint() throws IOException;

	class Factory {

		public static HubBuilderFactory getFactory(SnowflakeContext context) {
			Class<HubBuilderFactory> type = HubBuilderFactory.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

}
