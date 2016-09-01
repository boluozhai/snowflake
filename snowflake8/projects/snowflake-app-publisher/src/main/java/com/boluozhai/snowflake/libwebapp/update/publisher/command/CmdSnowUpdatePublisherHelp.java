package com.boluozhai.snowflake.libwebapp.update.publisher.command;

import java.io.PrintStream;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class CmdSnowUpdatePublisherHelp implements CLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		// TODO Auto-generated method stub

		final PrintStream out = System.out;

		out.format("usage: %s <command>\n", "snow-update-publisher");
		out.format("the list of commands:\n");

		final String cmdset = "    %s\n";

		out.format(cmdset, "help");
		out.format(cmdset, "loadapps");
		out.format(cmdset, "xxx");

	}

}
