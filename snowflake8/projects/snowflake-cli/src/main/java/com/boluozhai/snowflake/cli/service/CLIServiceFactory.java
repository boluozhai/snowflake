package com.boluozhai.snowflake.cli.service;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface CLIServiceFactory {

	CLIService create(SnowflakeContext context);

}
