package com.boluozhai.snow.cli.client;

import com.boluozhai.snowflake.context.SnowContext;

public interface CLIProcess extends Runnable {

	void start();

	void join() throws InterruptedException;

	// SnowContext parentContext();

	// SnowContext childContext();

	SnowContext context();

}
