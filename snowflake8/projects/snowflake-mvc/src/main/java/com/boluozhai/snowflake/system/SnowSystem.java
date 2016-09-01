package com.boluozhai.snowflake.system;

import com.boluozhai.snowflake.context.SnowflakeContext;

public abstract class SnowSystem implements SnowSystemAPI {

	public static SnowSystem getInstance(SnowflakeContext context) {
		SnowSystemFactory factory = SnowSystemFactory.getInstance(context);
		return factory.system(context);
	}

}
