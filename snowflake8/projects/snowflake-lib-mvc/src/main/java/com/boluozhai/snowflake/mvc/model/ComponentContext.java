package com.boluozhai.snowflake.mvc.model;

import com.boluozhai.snowflake.mvc.ModelContext;

public interface ComponentContext extends ModelContext {

	Component getRootComponent();

	Element getElement(String key);

}
