package com.boluozhai.snowflake.web.utils;

import javax.servlet.ServletContext;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

public class WebContextUtils extends SnowContextUtils {

	public static SnowContext getWebContext(ServletContext sc) {
		String key = WebContextUtils.class.getName() + "#snow_context";
		SnowContext snow_context = (SnowContext) sc.getAttribute(key);
		if (snow_context == null) {
			snow_context = getWebApplicationContext(sc);
			sc.setAttribute(key, snow_context);
		}
		return snow_context;
	}

}
