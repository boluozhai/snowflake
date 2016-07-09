package com.boluozhai.snowflake.spring.web;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snow.mvc.controller.ProcessContext;

public interface SpringMVC extends ProcessContext {

	ApplicationContext getSpringContext();

}
