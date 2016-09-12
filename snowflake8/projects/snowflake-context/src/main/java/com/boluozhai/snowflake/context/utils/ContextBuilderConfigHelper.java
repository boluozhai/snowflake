package com.boluozhai.snowflake.context.utils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.boluozhai.snowflake.context.ContextBuilder;

public final class ContextBuilderConfigHelper {

	public static void config_system_environments(ContextBuilder builder) {
		Map<String, String> env = System.getenv();
		Set<String> env_keys = env.keySet();
		for (String key : env_keys) {
			String value = env.get(key);
			builder.setEnvironment(key, value);
		}
	}

	public static void config_system_properties(ContextBuilder builder) {
		Properties prop = System.getProperties();
		Set<Object> prop_keys = prop.keySet();
		for (Object k : prop_keys) {
			String key = k.toString();
			String value = prop.getProperty(key);
			builder.setProperty(key, value);
		}
	}

	public static void config_app_arguments(ContextBuilder builder,
			String[] arguments) {

		if (arguments == null) {
			return;
		}
		ParameterRW rw = new ParameterRW(builder);
		int index = 0;
		for (String s : arguments) {
			final int i = (index++);
			if (s == null) {
				continue;
			}
			s = s.trim();
			if (s.length() == 0) {
				continue;
			}
			final String is = String.valueOf(i);
			if (s.startsWith("-")) {
				builder.setParameter(s, is);
			}
			rw.set(i, s);
		}

	}

	public static void config_all(ContextBuilder builder, String[] arguments) {

		config_system_environments(builder);
		config_system_properties(builder);
		config_app_arguments(builder, arguments);

	}

}
