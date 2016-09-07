package com.boluozhai.snowflake.h2o.command;

import java.io.PrintStream;
import java.util.Arrays;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLICommandDescription;
import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.service.CLIService;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class CmdHelp extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		CLIResponse resp = CLIResponse.Agent.getResponse(context);
		PrintStream out = resp.out();
		out.println("All Commands :");

		CLIService service = CLIUtils.getService(context);
		String[] list = service.getHandlerNames();
		Arrays.sort(list);
		for (String name : list) {
			final String text;
			CLICommandHandler h = service.getHandler(name);
			if (h instanceof CLICommandDescription) {
				CLICommandDescription desc = (CLICommandDescription) h;
				text = desc.getDescription();
			} else {
				text = "";
			}
			out.format("    %s    %12s\n", name, text);
		}

	}

}
