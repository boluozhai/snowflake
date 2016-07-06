package com.boluozhai.snow.system;

import com.boluozhai.snowflake.context.SnowContext;

public abstract class SnowSystemFactory {

	public static SnowSystemFactory getInstance(SnowContext context) {
		String key = SnowSystemFactory.class.getName();
		return context.getBean(key, SnowSystemFactory.class);
	}

	public abstract SnowSystem system(SnowContext context);

}
