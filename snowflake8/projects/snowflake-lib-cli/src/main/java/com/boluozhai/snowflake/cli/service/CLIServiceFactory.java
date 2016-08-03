package com.boluozhai.snowflake.cli.service;

import com.boluozhai.snowflake.context.SnowContext;

public interface CLIServiceFactory {

	CLIService create(SnowContext context);

}
