package com.boluozhai.snowflake.context;

import java.util.HashMap;
import java.util.Map;

public interface SnowParameters {

	Object exception = SnowContext.throw_exception_while_nil;

	String[] getParameterNames();

	String getParameter(String name);

	String getParameter(String name, Object defaultValue);

	class MapGetter {

		public static Map<String, String> getMap(SnowParameters src) {
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
