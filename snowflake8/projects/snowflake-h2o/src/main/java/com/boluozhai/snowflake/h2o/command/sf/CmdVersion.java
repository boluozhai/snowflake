package com.boluozhai.snowflake.h2o.command.sf;

import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class CmdVersion extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {
		// TODO Auto-generated method stub

		URI uri = context.getURI();

		CLIResponse resp = CLIResponse.Agent.getResponse(context);
		resp.out().println(uri);

	}

}
