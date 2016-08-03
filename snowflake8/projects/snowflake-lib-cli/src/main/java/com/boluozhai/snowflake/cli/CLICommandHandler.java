package com.boluozhai.snowflake.cli;

import com.boluozhai.snowflake.context.SnowContext;

public interface CLICommandHandler {

	void process(SnowContext context, String command);

}
