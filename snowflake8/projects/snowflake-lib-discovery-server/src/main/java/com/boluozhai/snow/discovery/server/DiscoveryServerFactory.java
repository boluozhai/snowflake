package com.boluozhai.snow.discovery.server;

import com.boluozhai.snowflake.context.SnowContext;

public interface DiscoveryServerFactory {

	DiscoveryServer getServer(SnowContext context);

}
