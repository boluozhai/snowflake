package com.boluozhai.snowflake.spring.web.context;

import org.springframework.web.context.WebApplicationContext;

import com.boluozhai.snowflake.mvc.controller.ProcessContext;
import com.boluozhai.snowflake.spring.SpringSnowContext;

public interface WebContext extends ProcessContext, SpringSnowContext {

	WebApplicationContext getSpringWebContext();

}
