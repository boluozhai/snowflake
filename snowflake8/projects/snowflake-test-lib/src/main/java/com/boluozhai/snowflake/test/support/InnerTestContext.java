package com.boluozhai.snowflake.test.support;

import java.io.File;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.base.AbstractContext;
import com.boluozhai.snowflake.context.base.SnowContextData;
import com.boluozhai.snowflake.test.TestContext;

public class InnerTestContext extends AbstractContext implements TestContext {

	public InnerTestContext(SnowContextData info, SnowContext sc, Object target) {
		super(info);
	}

	@Override
	public File getWorkingPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
