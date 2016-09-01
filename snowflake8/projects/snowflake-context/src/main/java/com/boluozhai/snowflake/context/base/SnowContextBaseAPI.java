package com.boluozhai.snowflake.context.base;

import com.boluozhai.snowflake.context.BeanFactory;
import com.boluozhai.snowflake.context.SnowflakeAttributes;
import com.boluozhai.snowflake.context.SnowflakeContextMeta;
import com.boluozhai.snowflake.context.SnowflakeEnvironments;
import com.boluozhai.snowflake.context.SnowflakeParameters;
import com.boluozhai.snowflake.context.SnowflakeProperties;

public interface SnowContextBaseAPI extends SnowflakeContextMeta, BeanFactory,
		SnowflakeProperties, SnowflakeAttributes, SnowflakeParameters, SnowflakeEnvironments {

}
