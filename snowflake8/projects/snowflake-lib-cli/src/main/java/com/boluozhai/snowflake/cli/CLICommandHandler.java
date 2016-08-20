package com.boluozhai.snowflake.cli;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CLICommandHandler {

	void process(SnowflakeContext context, String command);

}
