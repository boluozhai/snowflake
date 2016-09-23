package com.boluozhai.snowflake.cli.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.core.SnowflakeException;

public final class ParamSet {

	public static ParamSet create(ParamReader reader) {
		Map<String, Parameter> map = new HashMap<String, Parameter>();
		List<Parameter> list = new ArrayList<Parameter>();
		for (;;) {
			Parameter p = reader.read();
			if (p == null) {
				break;
			}
			if (p.isOption) {
				String key = p.name;
				map.put(key, p);
			} else {
				list.add(p);
			}
		}
		return new ParamSet(map, list);
	}

	private final Map<String, Parameter> _opts;
	private final List<Parameter> _args;

	private ParamSet(Map<String, Parameter> options, List<Parameter> args) {
		this._opts = options;
		this._args = args;
	}

	public String getOption(String name) {
		return this.getOption(name, null, false);
	}

	public String getOption(String name, String defaultValue) {
		return this.getOption(name, defaultValue, false);
	}

	public String getRequiredOption(String name) {
		return this.getOption(name, null, true);
	}

	public String getOption(String name, String defaultValue, boolean required) {
		Parameter op = this._opts.get(name);
		if (op == null) {
			if (required) {
				String msg = "need option: " + name;
				throw new SnowflakeException(msg);
			} else {
				return defaultValue;
			}
		} else {
			return op.value;
		}
	}

	public String getArgument(int index, String defaultValue, boolean required) {

		Parameter op = null;
		if (index < this._args.size()) {
			op = this._args.get(index);
		}

		if (op == null) {
			if (required) {
				String msg = "need argument[%d]";
				msg = String.format(msg, index);
				throw new SnowflakeException(msg);
			} else {
				return defaultValue;
			}
		} else {
			return op.value;
		}
	}

	public String getArgument(int index) {
		return this.getArgument(index, null, false);
	}

	public String getArgument(int index, String defaultValue) {
		return this.getArgument(index, defaultValue, false);
	}

	public String getRequiredArgument(int index) {
		return this.getArgument(index, null, true);
	}

}
