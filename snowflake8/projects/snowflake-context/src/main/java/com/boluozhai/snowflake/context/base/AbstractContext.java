package com.boluozhai.snowflake.context.base;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

public class AbstractContext extends SnowContextBase implements
		SnowflakeContext {

	protected AbstractContext(SnowContextData data) {
		super(data);
	}

	@Override
	public ContextBuilder child() {
		String fn = SnowContextUtils.FactoryName.child;
		return SnowContextUtils.getContextBuilder(this, fn);
	}

}
