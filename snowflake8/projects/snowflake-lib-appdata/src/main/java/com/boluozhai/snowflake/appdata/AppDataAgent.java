package com.boluozhai.snowflake.appdata;

import com.boluozhai.snowflake.context.SnowContext;

public abstract class AppDataAgent {

	public static AppDataAgent getAgent(SnowContext context) {
		String key = AppDataAgent.class.getName();
		return context.getBean(key, AppDataAgent.class);
	}

	public static AppData getAppData(SnowContext context, Class<?> clazz) {
		return getAgent(context).getAppData(clazz);
	}

	public abstract AppData getAppData(Class<?> clazz);

	public abstract AppData getAppData(Class<?> clazz, boolean throw_exception);

}
