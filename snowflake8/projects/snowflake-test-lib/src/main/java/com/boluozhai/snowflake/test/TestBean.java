package com.boluozhai.snowflake.test;

public class TestBean {

	private Tester tester;
	private Testing testing;

	public TestBean() {
	}

	public TestContext open(Object target) {
		this.tester = Tester.Factory.newInstance();
		this.testing = tester.open(target);
		return testing.context();
	}

	public void close() {
		tester.close(testing);
	}

}
