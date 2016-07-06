package com.boluozhai.snowflake.context.base;

import com.boluozhai.snowflake.context.BeanFactory;
import com.boluozhai.snowflake.context.SnowAttributes;
import com.boluozhai.snowflake.context.SnowContextMeta;
import com.boluozhai.snowflake.context.SnowEnvironments;
import com.boluozhai.snowflake.context.SnowParameters;
import com.boluozhai.snowflake.context.SnowProperties;

public interface SnowContextBaseAPI extends SnowContextMeta, BeanFactory,
		SnowProperties, SnowAttributes, SnowParameters, SnowEnvironments {

}
