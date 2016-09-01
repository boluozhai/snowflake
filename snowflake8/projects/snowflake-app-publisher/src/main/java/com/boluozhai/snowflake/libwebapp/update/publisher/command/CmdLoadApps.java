package com.boluozhai.snowflake.libwebapp.update.publisher.command;

import java.io.File;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.update.publisher.PublisherKit;

public class CmdLoadApps implements CLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		// TODO Auto-generated method stub

		// CLIResponse resp = CLIResponse.Agent.getResponse(context);
		// resp.setOutputObject("ok");

		PublisherKit pk = PublisherKit.Agent.getInstance(context);
		File m2dir = pk.getMavenM2dir();
		File appsFile = pk.getAppTableJsonFile();

		System.out.format("m2   : %s\n", m2dir);
		System.out.format("apps : %s\n", appsFile);

		// SnowUpdateTools tools = new SnowUpdateTools (up);
		// tools ... ;

	}
}
