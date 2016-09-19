package com.boluozhai.snowflake.access.security.auth;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface AuthManager {

	public static class Agent {

		public static AuthManager getInstance(SnowflakeContext context) {
			Class<AuthManager> type = AuthManager.class;
			String key = type.getName();
			return context.getBean(key, type);
		}

	}

	AuthInfo auth(AuthInfo info);

	AuthInfo getAuthInfo(AuthId id);

}
