package com.boluozhai.snowflake.discovery.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface DiscoveryClientFactory {

	DiscoveryClient getClient(SnowflakeContext context);

}
