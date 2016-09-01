package com.boluozhai.snowflake.xgit.command;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class TestGitInit {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			List<String> list = new ArrayList<String>();
			list.add("git init example.xgit --bare");
			list.add("git init example");

			CLIClient client = CLIUtils.getClient(context);
			for (String cmd : list) {
				client.execute(context, cmd);
			}

		} finally {
			tester.close(testing);
		}

	}

}
