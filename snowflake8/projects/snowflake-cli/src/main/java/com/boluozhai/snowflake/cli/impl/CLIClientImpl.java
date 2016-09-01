package com.boluozhai.snowflake.cli.impl;

import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.client.CLIProcess;
import com.boluozhai.snowflake.cli.client.ExecuteOption;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class CLIClientImpl implements CLIClient {

	public static CLIClient newInstance(SnowflakeContext context) {
		return new CLIClientImpl(context);
	}

	private CLIClientImpl(SnowflakeContext context) {
	}

	private CLIProcessBuilder new_proc_builder(SnowflakeContext context) {
		CLIProcessBuilder builder = new CLIProcessBuilder(context);
		builder.setAutoJoin(true);
		builder.setAutoStart(true);
		return builder;
	}

	@Override
	public CLIProcess execute(SnowflakeContext context, String cmd) {
		CLIProcessBuilder builder = this.new_proc_builder(context);
		builder.setCommand(cmd);
		return builder.create();
	}

	@Override
	public CLIProcess execute(SnowflakeContext context, String cmd,
			ExecuteOption option) {
		CLIProcessBuilder builder = this.new_proc_builder(context);
		builder.setCommand(cmd);
		builder.setOption(option);
		return builder.create();
	}

	@Override
	public CLIProcess execute(SnowflakeContext context, String cmd, String[] args) {
		CLIProcessBuilder builder = this.new_proc_builder(context);
		builder.setCommand(cmd);
		builder.setArguments(args);
		return builder.create();
	}

	@Override
	public CLIProcess execute(SnowflakeContext context, String cmd, String[] args,
			ExecuteOption option) {
		CLIProcessBuilder builder = this.new_proc_builder(context);
		builder.setCommand(cmd);
		builder.setArguments(args);
		builder.setOption(option);
		return builder.create();
	}

}
