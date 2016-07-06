package com.boluozhai.snow.cli.client;

import com.boluozhai.snow.context.SnowContext;

public interface CLIClientFactory {

	CLIClient create(SnowContext context);

}
