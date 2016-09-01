package com.boluozhai.snowflake.discovery.server;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DiscoveryServerFactory {

	DiscoveryServer getServer(SnowflakeContext context);

}
