package com.boluozhai.snow.appdata;

import com.boluozhai.snow.context.SnowContext;

public abstract class AppDataAgent {

	public static AppDataAgent getAgent(SnowContext context) {
		String key = AppDataAgent.class.getName();
		return context.getBean(key, AppDataAgent.class);
	}

	public static AppData getAppData(SnowContext context) {
		return getAgent(context).getAppData();
	}

	public abstract AppData getAppData();

}
