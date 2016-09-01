package com.boluozhai.snowflake.context;

import java.util.HashMap;
import java.util.Map;

public interface SnowflakeAttributes {

	Object exception = SnowflakeContext.throw_exception_while_nil;

	String[] getAttributeNames();

	Object getAttribute(String name);

	Object getAttribute(String name, Object defaultValue);

	class MapGetter {

		public static Map<String, Object> getMap(SnowflakeAttributes atts) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] keys = atts.getAttributeNames();
			for (String key : keys) {
				Object value = atts.getAttribute(key);
				map.put(key, value);
			}
			return map;
		}

	}

}
