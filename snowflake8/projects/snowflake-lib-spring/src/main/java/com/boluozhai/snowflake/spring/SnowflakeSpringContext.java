package com.boluozhai.snowflake.spring;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snow.mvc.controller.ProcessContext;

public interface SnowflakeSpringContext extends ProcessContext {

	ApplicationContext getSpringContext();

}
