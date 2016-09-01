package com.boluozhai.snowflake.test.all;

import java.io.File;

import org.junit.Test;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.test.support.DefaultTester;

public class TestAppData {

	@Test
	public void test() {

		Testing testing = null;
		Tester tester = new DefaultTester();

		try {
			testing = tester.open(this);

			TestContext context = testing.context();
			AppData ad = context.getAppData();

			File cp = ad.getCodePath();
			File pp = ad.getPropertiesPath();
			File dp = ad.getDataBasePath();
			File wp = context.getWorkingPath();

			System.out.format("code-path: %s\n", cp);
			System.out.format("prop-path: %s\n", pp);
			System.out.format("data-path: %s\n", dp);
			System.out.format("work-path: %s\n", wp);

		} finally {
			tester.close(testing);
		}
	}

}
