package com.boluozhai.snowflake.spring.impl;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.ContextBuilderConfigHelper;
import com.boluozhai.snowflake.spring.SpringContextLoader;
import com.boluozhai.snowflake.spring.SpringSnowContextBuilder;
import com.boluozhai.snowflake.spring.SpringSnowContextBuilderFactory;
import com.boluozhai.snowflake.spring.utils.SpringContextBuilderConfigHelper;

public class S2ContextBuilderFactoryImpl extends
		SpringSnowContextBuilderFactory {

	private final static SpringContextLoader spring_loader;

	static {
		spring_loader = new InnerSpringContextLoader();
	}

	@Override
	public SpringSnowContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public SpringSnowContextBuilder newBuilder(SnowContext parent) {
		ApplicationContext spring = spring_loader.get();
		SpringSnowContextBuilder builder = new InnerS2ContextBuilder(parent);
		builder.setSpringContext(spring);

		ContextBuilderConfigHelper.config_all(builder, null);
		SpringContextBuilderConfigHelper.config(builder, spring);

		return builder;
	}

}
