package com.boluozhai.snowflake.cli.client;

import com.boluozhai.snowflake.context.SnowContext;

public interface CLIClient {

	CLIProcess execute(SnowContext context, String cmd);

	CLIProcess execute(SnowContext context, String cmd, ExecuteOption option);

	CLIProcess execute(SnowContext context, String cmd, String[] args);

	CLIProcess execute(SnowContext context, String cmd, String[] args,
			ExecuteOption option);

}
