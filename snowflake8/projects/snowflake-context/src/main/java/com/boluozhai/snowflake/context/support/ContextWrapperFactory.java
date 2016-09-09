package com.boluozhai.snowflake.context.support;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface ContextWrapperFactory {

	SnowflakeContext wrap(SnowflakeContext context);

}
