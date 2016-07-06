package com.boluozhai.snow.context;

import java.util.Map;

public abstract class ContextBuilder implements MutableContextInfo,
		MutableAttributes, MutableProperties, MutableParameters {

	public abstract SnowContext create();

	public abstract Map<String, String> getParameters();

	public abstract Map<String, String> getProperties();

	public abstract Map<String, Object> getAttributes();

}
