package com.boluozhai.snow.cli.client;

import com.boluozhai.snow.context.SnowContext;

public interface CLIClient {

	CLIProcess execute(SnowContext context, String cmd);

	CLIProcess execute(SnowContext context, String cmd, ExecuteOption option);

	CLIProcess execute(SnowContext context, String cmd, String[] args);

	CLIProcess execute(SnowContext context, String cmd, String[] args,
			ExecuteOption option);

}
