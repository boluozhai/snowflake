package com.boluozhai.snowflake.cli.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.cli.CommandSet;
import com.boluozhai.snowflake.cli.impl.CLIServiceImpl;
import com.boluozhai.snowflake.cli.service.CLIService;
import com.boluozhai.snowflake.cli.service.CLIServiceFactory;
import com.boluozhai.snowflake.context.SnowContext;

public class DefaultCliServiceFactory implements CLIServiceFactory {

	private Map<String, CLICommandHandler> handlers;
	private Map<String, CLICommandHandler> _handlers_cache;

	@Override
	public CLIService create(SnowContext context) {
		Map<String, CLICommandHandler> map = this.get_handler_table(context);
		return CLIServiceImpl.newInstance(map);
	}

	private synchronized Map<String, CLICommandHandler> get_handler_table(
			SnowContext context) {
		Map<String, CLICommandHandler> table = this._handlers_cache;
		if (table == null) {

			final Map<String, CLICommandHandler> hds = this.handlers;
			if (hds == null) {
				table = new HashMap<String, CLICommandHandler>();
			} else {
				table = new HashMap<String, CLICommandHandler>(hds);
			}

			this.find_command_set(context, table);
			table = Collections.synchronizedMap(table);
			this._handlers_cache = table;
		}
		return table;
	}

	private void find_command_set(SnowContext context,
			Map<String, CLICommandHandler> table) {

		String[] keys = context.getAttributeNames();
		for (String key : keys) {
			Object bean = context.getAttribute(key);
			if (bean instanceof CommandSet) {
				CommandSet cs = (CommandSet) bean;
				this.load_command_set(key, cs, table);
			}
		}

	}

	private void load_command_set(String group_name, CommandSet cs,
			Map<String, CLICommandHandler> table) {

		if (!cs.isEnable()) {
			String msg = "Ignore DISABLED CommandSet: " + group_name;
			System.err.println(msg);
			return;
		}

		String prefix = cs.getPrefix();
		String suffix = cs.getSuffix();
		Map<String, CLICommandHandler> map = cs.getCommands();

		prefix = (prefix == null) ? "" : prefix;
		suffix = (suffix == null) ? "" : suffix;

		for (String key : map.keySet()) {

			final String key2 = prefix + key + suffix;
			final CLICommandHandler h = map.get(key);
			final CLICommandHandler old = table.get(key2);

			if (old == null) {
				table.put(key2, h);
			} else {
				String msg = "The command '%s' is override: old='%s', new='%s'\n";
				System.err.format(msg, key2, old.getClass(), h.getClass());
			}
		}

	}

	public Map<String, CLICommandHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, CLICommandHandler> handlers) {
		this.handlers = handlers;
	}

}
