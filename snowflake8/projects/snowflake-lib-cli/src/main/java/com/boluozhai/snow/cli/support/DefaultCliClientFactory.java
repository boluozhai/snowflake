package com.boluozhai.snow.cli.support;

import com.boluozhai.snow.cli.client.CLIClient;
import com.boluozhai.snow.cli.client.CLIClientFactory;
import com.boluozhai.snow.cli.impl.CLIClientImpl;
import com.boluozhai.snowflake.context.SnowContext;

public class DefaultCliClientFactory implements CLIClientFactory {

	@Override
	public CLIClient create(SnowContext context) {
		return CLIClientImpl.newInstance(context);
	}

}
