package com.boluozhai.snowflake.cli.client;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CLIProcess extends Runnable {

	void start();

	void join() throws InterruptedException;

	// SnowContext parentContext();

	// SnowContext childContext();

	SnowflakeContext context();

}
