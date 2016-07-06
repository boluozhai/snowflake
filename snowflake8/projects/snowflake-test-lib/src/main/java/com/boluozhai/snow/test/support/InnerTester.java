package com.boluozhai.snow.test.support;

import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.context.utils.SnowAppContextUtils;
import com.boluozhai.snow.test.Tester;
import com.boluozhai.snow.test.Testing;

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
