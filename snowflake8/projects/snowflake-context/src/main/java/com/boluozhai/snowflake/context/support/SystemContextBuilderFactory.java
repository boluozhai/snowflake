package com.boluozhai.snowflake.context.support;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowflakeContext;

public final class SystemContextBuilderFactory implements ContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowflakeContext parent) {
		ContextBuilderFactory factory = new DefaultContextBuilderFactory();
		ContextBuilder builder = factory.newBuilder(parent);

		Map<String, String> env = System.getenv();
		Properties prop = System.getProperties();

		try {
			Set<String> keys = env.keySet();
			for (String key : keys) {
				String value = env.get(key);
				builder.setEnvironment(key, value);
			}
		} finally {
		}

		try {
			Set<Object> keys = prop.keySet();
			for (Object k : keys) {
				String key = k.toString();
				String value = prop.getProperty(key);
				builder.setProperty(key, value);
			}
		} finally {
		}

		return builder;
	}

}
