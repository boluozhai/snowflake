package com.boluozhai.snowflake.spring;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.context.ContextBuilder;

public interface SpringSnowContextBuilder extends ContextBuilder {

	ApplicationContext getSpringContext();

	void setSpringContext(ApplicationContext ac);

}
