package com.boluozhai.snowflake.access.security.web.auth;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.server.RestController;

public interface WebAuthManager {

	public static class Agent {

		public static WebAuthManager getInstance(SnowflakeContext context) {
			Class<WebAuthManager> type = WebAuthManager.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

	RestController getHandler(String mach_name);

	String[] getHandlerNames();

}
