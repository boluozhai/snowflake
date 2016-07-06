package com.boluozhai.snow.context;

public interface SnowContext extends SnowContextBase, SnowContextInfo,
		SnowProperties, SnowAttributes, SnowParameters {

	/****
	 * use this object as the defaultValue , while get attr/prop/param, will get
	 * a exception while the request value is nil.
	 * */

	Object throw_exception_while_nil = new Object();

}
