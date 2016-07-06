package com.boluozhai.snowflake.context;

import java.util.Map;

public interface ContextBuilder extends MutableContextMeta, MutableAttributes,
		MutableProperties, MutableParameters, MutableEnvironments {

	SnowContext create();

	Map<String, String> getEnvironments();

	Map<String, String> getParameters();

	Map<String, String> getProperties();

	Map<String, Object> getAttributes();

}
