package com.boluozhai.snow.cli.client;

import com.boluozhai.snowflake.context.SnowContext;

public interface CLIProcess extends Runnable {

	void start();

	void join();

	SnowContext parentContext();

	SnowContext childContext();

}
