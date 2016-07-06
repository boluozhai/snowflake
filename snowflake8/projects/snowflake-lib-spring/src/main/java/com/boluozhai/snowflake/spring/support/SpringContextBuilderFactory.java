package com.boluozhai.snowflake.spring.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.spring.impl.SpringContextBuilder;

public class SpringContextBuilderFactory implements ContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {
		return SpringContextBuilder.newInstance(parent);
	}

}
