package com.boluozhai.snowflake.discovery.udp;

import com.boluozhai.snowflake.test.TestContext;

public interface HubBuilderFactory {

	HubBuilder newBuilder(TestContext context);

}
