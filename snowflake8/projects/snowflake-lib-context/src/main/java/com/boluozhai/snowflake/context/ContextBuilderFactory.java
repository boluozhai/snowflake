package com.boluozhai.snowflake.context;

public interface ContextBuilderFactory {

	ContextBuilder newBuilder();

	ContextBuilder newBuilder(SnowContext parent);

}
