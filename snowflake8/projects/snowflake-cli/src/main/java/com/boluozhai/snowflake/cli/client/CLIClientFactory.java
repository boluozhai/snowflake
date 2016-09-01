package com.boluozhai.snowflake.cli.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CLIClientFactory {

	CLIClient create(SnowflakeContext context);

}
