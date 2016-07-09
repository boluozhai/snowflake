package com.boluozhai.snowflake.test.support;

import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.test.impl.TesterImpl;

public final class DefaultTester implements Tester {

	private final Tester _inner;

	public DefaultTester() {
		_inner = new TesterImpl();
	}

	@Override
	public Testing open(Object target) {
		return _inner.open(target);
	}

	@Override
	public void close(Testing testing) {
		_inner.close(testing);
	}

}
