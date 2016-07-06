package com.boluozhai.snowflake.context;

public interface MutableContext extends SnowContext, MutableAttributes,
		MutableProperties, MutableParameters, MutableEnvironments,
		MutableContextMeta {

}
