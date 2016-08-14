package com.boluozhai.snowflake.discovery.client;

import com.boluozhai.snowflake.context.SnowContext;

public interface DiscoveryClient {

	class Factory {

		public static DiscoveryClient getInstance(SnowContext context) {
			Class<DiscoveryClientFactory> type = DiscoveryClientFactory.class;
			String key = type.getName();
			DiscoveryClientFactory factory = context.getBean(key, type);
			return factory.getClient(context);
		}

	}

}
