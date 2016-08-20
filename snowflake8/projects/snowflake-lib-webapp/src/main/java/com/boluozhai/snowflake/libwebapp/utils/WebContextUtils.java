package com.boluozhai.snowflake.libwebapp.utils;

import javax.servlet.ServletContext;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

public class WebContextUtils extends SnowContextUtils {

	public static SnowflakeContext getWebContext(ServletContext sc) {
		String key = WebContextUtils.class.getName() + "#snow_context";
		SnowflakeContext snow_context = (SnowflakeContext) sc.getAttribute(key);
		if (snow_context == null) {
			snow_context = getWebApplicationContext(sc);
			sc.setAttribute(key, snow_context);
		}
		return snow_context;
	}

}
