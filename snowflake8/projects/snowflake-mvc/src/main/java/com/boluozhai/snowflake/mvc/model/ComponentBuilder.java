package com.boluozhai.snowflake.mvc.model;

import com.boluozhai.snowflake.context.ContextBuilder;

public interface ComponentBuilder {

	Component create(ComponentContext cc, ContextBuilder cb);

}
