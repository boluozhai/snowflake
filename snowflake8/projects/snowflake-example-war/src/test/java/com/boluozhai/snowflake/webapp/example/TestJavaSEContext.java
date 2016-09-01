package com.boluozhai.snowflake.webapp.example;

import org.junit.Test;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.ContextPrinter;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

public class TestJavaSEContext {

	@Test
	public void test() {

		String[] arg = { "a", "b" };
		SnowflakeContext context = SnowContextUtils.getAppContext(
				TestJavaSEContext.class, arg);

		ContextPrinter.print(context, System.out);

	}

}
