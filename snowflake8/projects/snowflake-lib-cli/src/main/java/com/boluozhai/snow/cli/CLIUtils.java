package com.boluozhai.snow.cli;

import com.boluozhai.snow.cli.client.CLIClient;
import com.boluozhai.snow.cli.client.CLIClientFactory;
import com.boluozhai.snow.cli.service.CLIService;
import com.boluozhai.snow.cli.service.CLIServiceFactory;
import com.boluozhai.snow.context.SnowContext;

public class CLIUtils {

	public static CLIClientFactory getClientFactory(SnowContext context) {
		String key = CLIClientFactory.class.getName();
		return context.getBean(key, CLIClientFactory.class);
	}

	public static CLIServiceFactory getServiceFactory(SnowContext context) {
		String key = CLIServiceFactory.class.getName();
		return context.getBean(key, CLIServiceFactory.class);
	}

	public static CLIClient getClient(SnowContext context) {
		CLIClientFactory factory = getClientFactory(context);
		return factory.create(context);
	}

	public static CLIService getService(SnowContext context) {
		CLIServiceFactory factory = getServiceFactory(context);
		return factory.create(context);
	}

}
