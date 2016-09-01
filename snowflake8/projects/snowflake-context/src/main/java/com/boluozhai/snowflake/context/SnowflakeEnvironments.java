package com.boluozhai.snowflake.context;

import java.util.HashMap;
import java.util.Map;

public interface SnowflakeEnvironments {

	Object exception = SnowflakeContext.throw_exception_while_nil;

	String[] getEnvironmentNames();

	String getEnvironment(String name);

	String getEnvironment(String name, Object defaultValue);

	class MapGetter {

		public static Map<String, String> getMap(SnowflakeEnvironments src) {
			Map<String, String> map = new HashMap<String, String>();
			String[] keys = src.getEnvironmentNames();
			for (String key : keys) {
				String value = src.getEnvironment(key);
				map.put(key, value);
			}
			return map;
		}

	}

}
