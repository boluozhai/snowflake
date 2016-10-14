package com.boluozhai.snowflake.pojo.bridge;

import com.boluozhai.snowflake.context.SnowflakeContext;

public abstract class BridgeFactory {

	public static BridgeFactory getFactory(SnowflakeContext context) {
		String name = BridgeFactory.class.getName();
		return context.getBean(name, BridgeFactory.class);
	}

	/**
	 * @param context
	 *            the environment
	 * @param name
	 *            the bridge configuration name
	 * */

	public abstract Bridge create(SnowflakeContext context, String name);

}
