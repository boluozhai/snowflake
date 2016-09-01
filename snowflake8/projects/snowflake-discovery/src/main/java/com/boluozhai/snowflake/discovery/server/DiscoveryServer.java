package com.boluozhai.snowflake.discovery.server;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DiscoveryServer {

	class Factory {

		public static DiscoveryServer getInstance(SnowflakeContext context) {
			Class<DiscoveryServerFactory> type = DiscoveryServerFactory.class;
			String key = type.getName();
			DiscoveryServerFactory factory = context.getBean(key, type);
			return factory.getServer(context);
		}

	}

}
