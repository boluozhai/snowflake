package com.boluozhai.snow.cli.service;

import com.boluozhai.snow.cli.CLICommandHandler;

public interface CLIService {

	CLICommandHandler getHandler(String commandName);

	void setHandler(String commandName, CLICommandHandler h);

	String[] getHandlerNames();

}
