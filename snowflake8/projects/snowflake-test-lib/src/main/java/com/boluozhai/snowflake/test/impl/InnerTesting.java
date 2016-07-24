package com.boluozhai.snowflake.test.impl;

import java.io.File;

import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Testing;

final class InnerTesting implements Testing {

	private final TestContext _context;
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

	}

	@Override
	public void close() {

		String name = this.name();
		System.out.format("[end test %s]\n", name);
		System.out.println(bar);

	}

}
