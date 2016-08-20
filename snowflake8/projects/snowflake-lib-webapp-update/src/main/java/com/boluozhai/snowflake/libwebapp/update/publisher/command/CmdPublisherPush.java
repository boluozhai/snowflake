package com.boluozhai.snowflake.libwebapp.update.publisher.command;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.update.UpdatePublisherKit;

public class CmdPublisherPush implements CLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		UpdatePublisherKit kit = UpdatePublisherKit.Agent.getInstance(context);
		kit.push();

	}

}
