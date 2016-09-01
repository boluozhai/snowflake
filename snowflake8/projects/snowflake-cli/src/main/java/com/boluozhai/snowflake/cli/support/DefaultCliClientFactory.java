package com.boluozhai.snowflake.cli.support;

import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.client.CLIClientFactory;
import com.boluozhai.snowflake.cli.impl.CLIClientImpl;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class DefaultCliClientFactory implements CLIClientFactory {

	@Override
	public CLIClient create(SnowflakeContext context) {
		return CLIClientImpl.newInstance(context);
	}

}
