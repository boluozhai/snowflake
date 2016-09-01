package com.boluozhai.snowflake.spring.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.spring.impl.S2ContextBuilderFactoryImpl;

public class S2ContextBuilderFactory implements ContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowflakeContext parent) {
		S2ContextBuilderFactoryImpl factory = new S2ContextBuilderFactoryImpl();
		return factory.newBuilder(parent);
	}

}
