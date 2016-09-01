package com.boluozhai.snowflake.discovery.udp;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface HubBuilderFactory {

	HubBuilder newBuilder(SnowflakeContext context);

}
