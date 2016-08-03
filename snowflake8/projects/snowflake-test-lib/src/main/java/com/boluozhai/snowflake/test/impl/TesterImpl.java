package com.boluozhai.snowflake.test.impl;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public final class TesterImpl implements Tester {

	public TesterImpl() {

	}

	@Override
	public Testing open(Object target) {

		SnowContext context = SnowContextUtils.getJunitContext(target);
		TestContextWrapper tc = new TestContextWrapper(context, target);
		InnerTesting testing = new InnerTesting(tc, target);
		testing.open();
		return testing;

	}

	@Override
	public void close(Testing testing) {
		if (testing == null) {
			return;
		} else {
			testing.close();
		}
	}

}
