package com.boluozhai.snowflake.runtime.support;

import com.boluozhai.snowflake.runtime.RuntimeExe;
import com.boluozhai.snowflake.runtime.SubProcessBuilder;
import com.boluozhai.snowflake.runtime.impl.SubProcessBuilderImpl;

public class DefaultRuntimeExe implements RuntimeExe {

	@Override
	public SubProcessBuilder newSubProcessBuilder() {
		return new SubProcessBuilderImpl();
	}

}
