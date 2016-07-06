package com.boluozhai.snow.test.support;

import java.io.File;

import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.context.support.AbstractContext;
import com.boluozhai.snow.context.support.ContextInfo;
import com.boluozhai.snow.test.TestContext;

public class InnerTestContext extends AbstractContext implements TestContext {

	public InnerTestContext(ContextInfo info, SnowContext sc, Object target) {
		// TODO Auto-generated constructor stub

		super(info);

	}

	@Override
	public File getWorkingPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
