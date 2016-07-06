package com.boluozhai.snow.cli;

import com.boluozhai.snow.cli.client.CLIClient;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowAppContextUtils;

public class SnowCLI {

	public static void main(String[] args) {

		// TODO debug
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			System.out.format("debug.arg[%d] = %s\n", i, s);
		}

		SnowContext context = SnowAppContextUtils.getContext();
		CLIClient client = CLIUtils.getClient(context);

	}
}
