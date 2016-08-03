package com.boluozhai.snowflake.cli.support;

import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.client.CLIClientFactory;
import com.boluozhai.snowflake.cli.impl.CLIClientImpl;
import com.boluozhai.snowflake.context.SnowContext;

public class DefaultCliClientFactory implements CLIClientFactory {

	@Override
	public CLIClient create(SnowContext context) {
		return CLIClientImpl.newInstance(context);
	}

}
