package com.boluozhai.snowflake.discovery.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DiscoveryClient {

	class Factory {

		public static DiscoveryClient getInstance(SnowflakeContext context) {
			Class<DiscoveryClientFactory> type = DiscoveryClientFactory.class;
			String key = type.getName();
			DiscoveryClientFactory factory = context.getBean(key, type);
			return factory.getClient(context);
		}

	}

}
