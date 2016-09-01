package com.boluozhai.snowflake.context;

public interface MutableContext extends SnowflakeContext, MutableAttributes,
		MutableProperties, MutableParameters, MutableEnvironments,
		MutableContextMeta {

}
