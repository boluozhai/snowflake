package com.boluozhai.snow.mvc.model;

import com.boluozhai.snow.mvc.ModelContext;

public interface ComponentContext extends ModelContext {

	Component getRootComponent();

	Element getElement(String key);

}
