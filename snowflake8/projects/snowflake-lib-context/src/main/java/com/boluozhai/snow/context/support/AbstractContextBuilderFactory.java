package com.boluozhai.snow.context.support;

import com.boluozhai.snow.context.ContextBuilder;
import com.boluozhai.snow.context.ContextBuilderFactory;
import com.boluozhai.snow.context.SnowContext;

public class AbstractContextBuilderFactory implements ContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {
		return new AbstractContextBuilder(parent);
	}

}
