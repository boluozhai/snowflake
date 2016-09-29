package com.boluozhai.snowflake.xgit.command;

import java.io.PrintStream;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class XGitLLPush extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		CLIResponse cli = CLIResponse.Agent.getResponse(context);
		PrintStream out = cli.out();

		// a object

		// a blob

		// a set of blobs

		// a set of trees

		// a set of commits

		out.println("done.");

	}

}
