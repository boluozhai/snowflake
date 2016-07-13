package com.boluozhai.snow.mvc.model;

import com.boluozhai.snowflake.context.ContextBuilder;

public interface ComponentBuilder {

	Component create(ComponentContext cc, ContextBuilder cb);

}
