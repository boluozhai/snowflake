package com.boluozhai.snowflake.context;

public interface SnowContext extends SnowContextMeta, BeanFactory,
		SnowProperties, SnowAttributes, SnowParameters, SnowEnvironments {

	/****
	 * use this object as the defaultValue , while get attr/prop/param, will get
	 * a exception while the request value is nil.
	 * */

	static final Object throw_exception_while_nil = new Object();

}
