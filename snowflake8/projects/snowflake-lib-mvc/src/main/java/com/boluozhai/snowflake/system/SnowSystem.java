package com.boluozhai.snowflake.system;

import com.boluozhai.snowflake.context.SnowContext;

public abstract class SnowSystem implements SnowSystemAPI {

	public static SnowSystem getInstance(SnowContext context) {
		SnowSystemFactory factory = SnowSystemFactory.getInstance(context);
		return factory.system(context);
	}

}
