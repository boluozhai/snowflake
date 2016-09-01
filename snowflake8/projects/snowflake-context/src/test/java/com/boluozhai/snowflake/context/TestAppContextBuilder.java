package com.boluozhai.snowflake.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.boluozhai.snowflake.context.support.DefaultContextBuilderFactory;
import com.boluozhai.snowflake.context.utils.ContextBuilderConfigHelper;

public class TestAppContextBuilder {

	@Test
	public void test() {

		ContextBuilderFactory factory = new DefaultContextBuilderFactory();
		ContextBuilder builder = factory.newBuilder();
		this.config(builder);
		SnowflakeContext context = builder.create();
		this.log(context);

	}

	private void log(SnowflakeContext context) {

		System.out.println(this);
		this.log_kv("uri", context.getURI());
		this.log_kv("name", context.getName());
		this.log_kv("description", context.getDescription());
		this.log_kv("birthday", context.getBirthday());

		System.out.println();
		System.out.println("[env]");
		this.log_map(SnowflakeEnvironments.MapGetter.getMap(context));

		System.out.println();
		System.out.println("[properties]");
		this.log_map(SnowflakeProperties.MapGetter.getMap(context));

		System.out.println();
		System.out.println("[param]");
		this.log_map(SnowflakeParameters.MapGetter.getMap(context));

	}

	private void log_kv(String key, Object value) {
		System.out.format("  %s = %s\n", key, value);
	}

	private void log_map(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = map.get(key);
			this.log_kv(key, value);
		}
	}

	private void config(ContextBuilder builder) {

		// log command-line

		System.out.println("config with command line:");
		String[] arguments = { "git", "status", "-a", "-b", "-m", "xyz", "eof", };
		for (String s : arguments) {
			System.out.format(" %s", s);
		}
		System.out.println();

		// config with helper

		ContextBuilderConfigHelper.config_all(builder, arguments);

		// config self

		URI uri = null;
		try {
			uri = this.getClass().getProtectionDomain().getCodeSource()
					.getLocation().toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		builder.setName(this.getClass().getSimpleName());
		builder.setDescription(this.getClass().getName());
		builder.setURI(uri);

	}

}
