package com.boluozhai.snowflake.appdata;

import com.boluozhai.snowflake.context.SnowflakeContext;

public abstract class AppDataAgent {

	public static AppDataAgent getAgent(SnowflakeContext context) {
		String key = AppDataAgent.class.getName();
		return context.getBean(key, AppDataAgent.class);
	}

	public static AppData getAppData(SnowflakeContext context, Class<?> clazz) {
		return getAgent(context).getAppData(clazz);
	}

	public abstract AppData getAppData(Class<?> clazz);

	public abstract AppData getAppData(Class<?> clazz, boolean throw_exception);

}
