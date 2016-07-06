package com.boluozhai.snow.test.support;

import com.boluozhai.snow.test.Tester;
import com.boluozhai.snow.test.Testing;

public final class DefaultTester implements Tester {

	private final Tester _inner;

	public DefaultTester() {
		_inner = new InnerTester();
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
