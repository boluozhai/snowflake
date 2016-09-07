package com.boluozhai.snowflake.cli.support;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class SubCommandSelector extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		String op = context.getParameter("0");
		String cmd2 = command + "-" + op;
		String[] args = CLIUtils.getArguments(context, 1, -1);

		CLIClient cli = CLIUtils.getClient(context);
		cli.execute(context, cmd2, args);

	}

}
