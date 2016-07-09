package com.boluozhai.snowflake.test.impl;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public final class TesterImpl implements Tester, ContextBuilderFactory {

	public TesterImpl() {

		this.init_properties();

	}

	@Override
	public Testing open(Object target) {

		ContextBuilder builder = this.newBuilder();
		SnowContext sc = builder.create();
		TestContextWrapper tc = new TestContextWrapper(sc, target);

		InnerTesting testing = new InnerTesting(tc, target);
		testing.open();
		return testing;

	}

	private void init_properties() {

		String key, value;

		key = ContextBuilderFactory.class.getName();
		value = "com.boluozhai.snowflake.spring.support.S2ContextBuilderFactory";
		System.setProperty(key, value);

		key = "com.boluozhai.snowflake.spring.SpringContextLoader_xml";
		value = "classpath:config/spring/test.xml";
		System.setProperty(key, value);

	}

	@Override
	public void close(Testing testing) {
		if (testing == null) {
			return;
		} else {
			testing.close();
		}
	}

	@Override
	public ContextBuilder newBuilder() {
		return this.newBuilder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {

		ContextBuilderFactory factory = SnowContextUtils
				.getContextBuilderFactory();

		return factory.newBuilder(parent);
	}

}
