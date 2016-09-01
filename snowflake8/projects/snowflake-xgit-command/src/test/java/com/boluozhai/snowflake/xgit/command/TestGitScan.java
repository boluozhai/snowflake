package com.boluozhai.snowflake.xgit.command;

import org.junit.Test;

import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class TestGitScan {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			String cmd = "xgit scan";

			CLIClient client = CLIUtils.getClient(context);
			client.execute(context, cmd);

		} finally {
			tester.close(testing);
		}

	}

}
