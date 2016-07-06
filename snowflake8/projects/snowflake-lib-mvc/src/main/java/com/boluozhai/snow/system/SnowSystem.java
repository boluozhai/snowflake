package com.boluozhai.snow.system;

import com.boluozhai.snow.context.SnowContext;

public abstract class SnowSystem implements SnowSystemAPI {

	public static SnowSystem getInstance(SnowContext context) {
		SnowSystemFactory factory = SnowSystemFactory.getInstance(context);
		return factory.system(context);
	}

}
