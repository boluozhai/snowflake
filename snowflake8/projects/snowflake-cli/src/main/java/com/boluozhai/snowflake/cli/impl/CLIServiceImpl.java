package com.boluozhai.snowflake.cli.impl;

import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.cli.service.CLIService;

public class CLIServiceImpl implements CLIService {

	private final Map<String, CLICommandHandler> _handlers;

	public CLIServiceImpl(Map<String, CLICommandHandler> map) {
		this._handlers = map;
	}

	public static CLIService newInstance(Map<String, CLICommandHandler> map) {
		return new CLIServiceImpl(map);
	}

	@Override
	public CLICommandHandler getHandler(String commandName) {
		return this._handlers.get(commandName);
	}

	@Override
	public void setHandler(String commandName, CLICommandHandler h) {
		this._handlers.put(commandName, h);
	}

	@Override
	public String[] getHandlerNames() {
		Set<String> keys = this._handlers.keySet();
		return keys.toArray(new String[keys.size()]);
	}

}
