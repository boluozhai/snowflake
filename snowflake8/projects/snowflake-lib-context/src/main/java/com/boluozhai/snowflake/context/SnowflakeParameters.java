package com.boluozhai.snowflake.context;

import java.util.HashMap;
import java.util.Map;

public interface SnowflakeParameters {

	Object exception = SnowflakeContext.throw_exception_while_nil;

	String[] getParameterNames();

	String getParameter(String name);

	String getParameter(String name, Object defaultValue);

	class MapGetter {

		public static Map<String, String> getMap(SnowflakeParameters src) {
			Map<String, String> map = new HashMap<String, String>();
			String[] keys = src.getParameterNames();
			for (String key : keys) {
				String value = src.getParameter(key);
				map.put(key, value);
			}
			return map;
		}

	}

}
