package com.boluozhai.snowflake.xgit.vfs.impl.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snow.vfs.VFile;
import com.boluozhai.snowflake.context.SnowProperties;

public class FileRepoConfigImpl {

	private final Map<String, String> table;

	public FileRepoConfigImpl() {
		table = new HashMap<String, String>();
	}

	public void load(ComponentContext context, VFile file) throws IOException {
		ConfigLoader loader = new ConfigLoader(context);
		Map<String, String> map = loader.load(file);
		table.clear();
		table.putAll(map);
	}

	public void save(ComponentContext context, VFile file) throws IOException {
		ConfigSaver saver = new ConfigSaver(context);
		saver.save(table, file);
	}

	public void put(String key, String value) {
		table.put(key, value);
	}

	public String[] keys() {
		Set<String> keys = table.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	public String get(String name) {
		Object def = SnowProperties.exception;
		return this.get(name, def);
	}

	public String get(String key, Object def) {
		String value = table.get(key);
		if (value == null) {
			if (def == null) {
				// NOP
			} else if (def == SnowProperties.exception) {
				String msg = "no value for key: " + key;
				throw new RuntimeException(msg);
			} else {
				value = def.toString();
			}
		}
		return value;
	}

}
