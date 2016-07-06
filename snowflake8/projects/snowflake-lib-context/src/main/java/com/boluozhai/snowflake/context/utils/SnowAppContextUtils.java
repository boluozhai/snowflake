package com.boluozhai.snowflake.context.utils;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.AppContextBuilderFactory;

public final class SnowAppContextUtils {

	private SnowAppContextUtils() {
	}

	public static ContextBuilderFactory getContextBuilderFactory() {
		return new AppContextBuilderFactory();
	}

	public static ContextBuilder getContextBuilder() {
		return getContextBuilderFactory().newBuilder();
	}

	public static SnowContext getContext() {
		return getContextBuilder().create();
	}

}
