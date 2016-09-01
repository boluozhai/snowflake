package com.boluozhai.snowflake.cli.service;

import com.boluozhai.snowflake.cli.CLICommandHandler;

public interface CLIService {

	CLICommandHandler getHandler(String commandName);

	void setHandler(String commandName, CLICommandHandler h);

	String[] getHandlerNames();

}
