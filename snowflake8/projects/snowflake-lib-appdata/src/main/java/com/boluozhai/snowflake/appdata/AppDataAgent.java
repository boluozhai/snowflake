package com.boluozhai.snowflake.appdata;

import com.boluozhai.snowflake.context.SnowContext;

public abstract class AppDataAgent {

	public static AppDataAgent getAgent(SnowContext context) {
		String key = AppDataAgent.class.getName();
		return context.getBean(key, AppDataAgent.class);
	}

	public static AppData getAppData(SnowContext context) {
		return getAgent(context).getAppData();
	}

	public abstract AppData getAppData();

	public abstract AppData getAppData(boolean throw_exception);

}
