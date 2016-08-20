package com.boluozhai.snowflake.test.impl;

import java.io.File;
import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Testing;

final class InnerTesting implements Testing {

	private TestContext _context;
	private final Object _target;

	public InnerTesting(TestContext sc, Object target) {
		this._context = sc;
		this._target = target;
	}

	@Override
	public TestContext context() {
		return this._context;
	}

	@Override
	public String name() {
		String name = this.context().getName();
		if (name == null) {
			name = this._target + "";
		}
		return name;
	}

	final static String bar1 = "============================================================================================\n";
	final static String bar = bar1 + bar1 + bar1;

	@Override
	public void open() {

		String name = this.name();
		System.out.println(bar);
		System.out.format("[begin test %s]\n", name);

		System.out.println("[prepare testing environment]");

		File wk_dir = this.context().getWorkingPath();
		File temp_dir = this.context().getWorksTemplatePath();

		// clean working directory
		DirTools.clean(wk_dir);

		// copy data to working directory
		DirTools.copy(temp_dir, wk_dir);

		System.out.println("[testing...]");

		// update context
		this.inner_update_context(wk_dir);

	}

	private void inner_update_context(File wk_dir) {

		URI uri = wk_dir.toURI();
		TestContext context = this._context;
		Object target = context.getTestTarget();
		this._context = new MyContext(context, target, uri);

	}

	private class MyContext extends TestContextWrapper {

		private final URI _uri;

		public MyContext(SnowflakeContext context, Object target, URI uri) {
			super(context, target);
			this._uri = uri;
		}

		@Override
		public URI getURI() {
			return this._uri;
		}

	}

	@Override
	public void close() {

		String name = this.name();
		System.out.format("[end test %s]\n", name);
		System.out.println(bar);

	}

}
