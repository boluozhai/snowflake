package com.boluozhai.snowflake.context;

import java.util.HashMap;
import java.util.Map;

public interface SnowflakeProperties {

	Object exception = SnowflakeContext.throw_exception_while_nil;

	String[] getPropertyNames();

	String getProperty(String name);

	String getProperty(String name, Object defaultValue);

	class MapGetter {

		public static Map<String, String> getMap(SnowflakeProperties src) {
			Map<String, String> map = new HashMap<String, String>();
			String[] keys = src.getPropertyNames();
			for (String key : keys) {
				String value = src.getProperty(key);
				map.put(key, value);
			}
			return map;
		}

	}

}
