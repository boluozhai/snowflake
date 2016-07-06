package com.boluozhai.snowflake.test.support;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Testing;

final class InnerTesting implements Testing {

	private TestContext _context;

	public InnerTesting(SnowContext sc, Object target) {
		this._context = new InnerTestContext(sc, target);
	}

	@Override
	public TestContext context() {
		return this._context;
	}

	@Override
	public String name() {
		return this.context().getName();
	}

	final static String bar = "/***********************************/";

	@Override
	public void open() {

		String name = this.name();
		System.out.println(bar);
		System.out.println("test " + name + " {");

	}

	@Override
	public void close() {

		String name = this.name();
		System.out.println("}; //" + name);
		System.out.println(bar);

	}

}
