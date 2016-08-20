package com.boluozhai.snowflake.system;

import com.boluozhai.snowflake.context.SnowflakeContext;

public abstract class SnowSystemFactory {

	public static SnowSystemFactory getInstance(SnowflakeContext context) {
		String key = SnowSystemFactory.class.getName();
		return context.getBean(key, SnowSystemFactory.class);
	}

	public abstract SnowSystem system(SnowflakeContext context);

}
