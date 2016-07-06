package com.boluozhai.snow.test.all;

import java.io.File;

import org.junit.Test;

import com.boluozhai.snow.appdata.AppData;
import com.boluozhai.snow.test.TestContext;
import com.boluozhai.snow.test.Tester;
import com.boluozhai.snow.test.Testing;
import com.boluozhai.snow.test.support.DefaultTester;

public class TestAppData {

	@Test
	public void test() {

		Testing testing = null;
		Tester tester = new DefaultTester();

		try {
			testing = tester.open(this);

			TestContext context = testing.context();
			AppData ad = AppData.Helper.getInstance(context);

			File cp = ad.getCodePath();
			File pp = ad.getPropertiesPath();
			File dp = ad.getDataPath();

			System.out.format("code-path: %s\n", cp);
			System.out.format("prop-path: %s\n", pp);
			System.out.format("data-path: %s\n", dp);

		} finally {
			tester.close(testing);
		}
	}

}
