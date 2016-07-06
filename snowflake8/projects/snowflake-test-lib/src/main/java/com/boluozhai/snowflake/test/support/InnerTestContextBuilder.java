package com.boluozhai.snowflake.test.support;

import java.io.File;

import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.context.base.SnowContextData;

public class InnerTestContextBuilder extends AbstractContextBuilder {

	protected InnerTestContextBuilder(SnowContextData data) {
		super(data);
	}

	@Override
	public File getWorkingPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
