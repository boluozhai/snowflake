package com.boluozhai.snow.discovery.server;

import com.boluozhai.snowflake.context.SnowContext;

public interface DiscoveryServer {

	class Factory {

		public static DiscoveryServer getInstance(SnowContext context) {
			Class<DiscoveryServerFactory> type = DiscoveryServerFactory.class;
			String key = type.getName();
			DiscoveryServerFactory factory = context.getBean(key, type);
			return factory.getServer(context);
		}

	}

}
