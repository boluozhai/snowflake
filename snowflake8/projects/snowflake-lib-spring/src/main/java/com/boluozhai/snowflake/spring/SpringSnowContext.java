package com.boluozhai.snowflake.spring;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snowflake.mvc.controller.ProcessContext;

public interface SpringSnowContext extends ProcessContext {

	ApplicationContext getSpringContext();

}
