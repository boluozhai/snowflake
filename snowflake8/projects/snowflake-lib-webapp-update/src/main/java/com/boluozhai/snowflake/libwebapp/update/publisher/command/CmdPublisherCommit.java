package com.boluozhai.snowflake.libwebapp.update.publisher.command;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.update.UpdatePublisherKit;

public class CmdPublisherCommit implements CLICommandHandler {

	@Override
	public void process(SnowContext context, String command) {

		UpdatePublisherKit kit = UpdatePublisherKit.Agent.getInstance(context);
		kit.commit();

	}

}