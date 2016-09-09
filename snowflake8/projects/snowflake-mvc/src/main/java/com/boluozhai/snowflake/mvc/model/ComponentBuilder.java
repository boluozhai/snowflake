package com.boluozhai.snowflake.mvc.model;

import com.boluozhai.snowflake.context.ContextBuilder;

public interface ComponentBuilder {

	void configure(ContextBuilder cb);

	Component create(ComponentContext cc);

}
