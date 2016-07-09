package com.boluozhai.snowflake.test.impl;

import java.io.File;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.test.TestContext;

public class TestContextWrapper extends ContextWrapper implements TestContext {

	private final Object _test_target;
	private File _working_path;

	public TestContextWrapper(SnowContext context, Object test_target) {
		super(context);
		this._test_target = test_target;
	}

	@Override
	public File getWorkingPath() {
		File wp = this._working_path;
		if (wp == null) {
			Class<?> schema = this._test_target.getClass();
			AppData ad = AppData.Helper.getInstance(this);
			wp = ad.getDataSchemaPath(schema);
			this._working_path = wp;
		}
		return wp;
	}

	@Override
	public Object getTestTarget() {
		return this._test_target;
	}

}
