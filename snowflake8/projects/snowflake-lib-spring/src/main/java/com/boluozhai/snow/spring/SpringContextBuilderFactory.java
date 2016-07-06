package com.boluozhai.snow.spring;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snow.context.ContextBuilder;
import com.boluozhai.snow.context.ContextBuilderFactory;
import com.boluozhai.snow.context.SnowContext;

public class SpringContextBuilderFactory implements ContextBuilderFactory {

	private static final SpringContextLoader loader;

	static {
		loader = new SpringContextLoader();
	}

	public ApplicationContext getSpringContext() {
		return loader.get();
	}

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {
		ApplicationContext ac = getSpringContext();
		return new SpringContextBuilder(parent, ac);
	}

}
