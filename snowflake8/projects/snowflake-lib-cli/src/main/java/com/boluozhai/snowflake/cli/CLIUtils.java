package com.boluozhai.snowflake.cli;

import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.client.CLIClientFactory;
import com.boluozhai.snowflake.cli.service.CLIService;
import com.boluozhai.snowflake.cli.service.CLIServiceFactory;
import com.boluozhai.snowflake.context.SnowContext;

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

	public static String[] getArguments(SnowContext context, int offset,
			int length) {

		if (length < 0) {
			length = 0xffff;
		}
		final int end = offset + length;
		List<String> list = new ArrayList<String>();

		for (int i = offset; i < end; i++) {
			String key = String.format("%d", i);
			String val = context.getParameter(key, null);
			if (val == null) {
				break;
			} else {
				list.add(val);
			}
		}

		return list.toArray(new String[list.size()]);
	}

}
