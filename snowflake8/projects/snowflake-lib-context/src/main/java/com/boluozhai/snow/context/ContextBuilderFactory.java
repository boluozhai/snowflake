package com.boluozhai.snow.context;

public interface ContextBuilderFactory {

	ContextBuilder newBuilder();

	ContextBuilder newBuilder(SnowContext parent);

}
