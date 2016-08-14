package com.boluozhai.snowflake.discovery.client;

import com.boluozhai.snowflake.context.SnowContext;

public interface DiscoveryClientFactory {

	DiscoveryClient getClient(SnowContext context);

}
