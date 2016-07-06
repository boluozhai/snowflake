package com.boluozhai.snowflake.test.support;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowAppContextUtils;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

final class InnerTester implements Tester {

	public InnerTester() {
	}

	@Override
	public Testing open(Object target) {
		SnowContext sc = SnowAppContextUtils.getContext();
		InnerTesting testing = new InnerTesting(sc, target);
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
