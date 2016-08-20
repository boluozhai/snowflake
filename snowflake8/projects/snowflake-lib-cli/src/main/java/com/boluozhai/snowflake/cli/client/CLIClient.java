package com.boluozhai.snowflake.cli.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CLIClient {

	CLIProcess execute(SnowflakeContext context, String cmd);

	CLIProcess execute(SnowflakeContext context, String cmd, ExecuteOption option);

	CLIProcess execute(SnowflakeContext context, String cmd, String[] args);

	CLIProcess execute(SnowflakeContext context, String cmd, String[] args,
			ExecuteOption option);

}
