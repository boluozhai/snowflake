package com.boluozhai.snow.context.utils;

import com.boluozhai.snow.context.ContextBuilder;
import com.boluozhai.snow.context.ContextBuilderFactory;
import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.context.support.AppContextBuilderFactory;

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
