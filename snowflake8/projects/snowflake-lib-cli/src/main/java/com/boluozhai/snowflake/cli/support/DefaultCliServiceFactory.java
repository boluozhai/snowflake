package com.boluozhai.snowflake.cli.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.cli.impl.CLIServiceImpl;
import com.boluozhai.snowflake.cli.service.CLIService;
import com.boluozhai.snowflake.cli.service.CLIServiceFactory;
import com.boluozhai.snowflake.context.SnowContext;

public class DefaultCliServiceFactory implements CLIServiceFactory {

	private Map<String, CLICommandHandler> handlers;
	private Map<String, CLICommandHandler> _handlers_cache;

	@Override
	public CLIService create(SnowContext context) {
		Map<String, CLICommandHandler> map = this.get_handler_table();
		return CLIServiceImpl.newInstance(map);
	}

	private synchronized Map<String, CLICommandHandler> get_handler_table() {
		Map<String, CLICommandHandler> table = this._handlers_cache;
		if (table == null) {
			table = new HashMap<String, CLICommandHandler>(this.handlers);
			table = Collections.synchronizedMap(table);
			this._handlers_cache = table;
		}
		return table;
	}

	public Map<String, CLICommandHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, CLICommandHandler> handlers) {
		this.handlers = handlers;
	}

}
