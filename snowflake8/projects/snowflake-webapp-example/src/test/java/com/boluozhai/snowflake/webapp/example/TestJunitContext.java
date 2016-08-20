package com.boluozhai.snowflake.webapp.example;

import org.junit.Test;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.ContextPrinter;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class TestJunitContext {

	@Test
	public void test1() {

		SnowflakeContext context = SnowContextUtils.getJunitContext(this);
		ContextPrinter.print(context, System.out);

	}

	@Test
	public void test2() {

		Tester tester = null;
		Testing testing = null;
		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);

			TestContext context = testing.context();
			ContextPrinter.print(context, System.out);

		} finally {
			tester.close(testing);
		}

	}

}
