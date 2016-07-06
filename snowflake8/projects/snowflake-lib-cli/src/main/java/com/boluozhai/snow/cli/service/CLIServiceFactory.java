package com.boluozhai.snow.cli.service;

import com.boluozhai.snow.context.SnowContext;

public interface CLIServiceFactory {

	CLIService create(SnowContext context);

}
