package com.boluozhai.snowflake.context.base;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class AbstractContextBuilderFactory implements ContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowflakeContext parent) {
		return new AbstractContextBuilder(parent);
	}

}
