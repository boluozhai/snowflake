package com.boluozhai.snowflake.spring;

import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.spring.impl.S2ContextBuilderFactoryImpl;

public abstract class SpringSnowContextBuilderFactory implements
		ContextBuilderFactory {

	public static SpringSnowContextBuilderFactory newInstance() {
		return new S2ContextBuilderFactoryImpl();
	}

	public static SpringSnowContextBuilder newContextBuilder() {
		return newInstance().newBuilder();
	}

	public abstract SpringSnowContextBuilder newBuilder();

	public abstract SpringSnowContextBuilder newBuilder(SnowflakeContext parent);

}
