package com.boluozhai.snowflake.cli;

import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

public class SnowCLI {

	public static void main(String[] args) {

		// TODO debug
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			System.out.format("debug.arg[%d] = %s\n", i, s);
		}

		String fn = SnowContextUtils.FactoryName.javase;
		SnowContext context = SnowContextUtils.getContext(fn);
		CLIClient client = CLIUtils.getClient(context);
		client.execute(context, "snow", args);

	}
}
