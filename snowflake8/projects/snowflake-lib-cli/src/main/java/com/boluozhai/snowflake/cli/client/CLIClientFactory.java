package com.boluozhai.snowflake.cli.client;

import com.boluozhai.snowflake.context.SnowContext;

public interface CLIClientFactory {

	CLIClient create(SnowContext context);

}
