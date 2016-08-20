package com.boluozhai.snowflake.context.base;

import com.boluozhai.snowflake.context.SnowflakeContext;

public class AbstractContext extends SnowContextBase implements SnowflakeContext {

	protected AbstractContext(SnowContextData data) {
		super(data);
	}

}
