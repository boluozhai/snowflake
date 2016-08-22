package com.boluozhai.snowflake.xgit.command;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class TestGitCommit {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			List<String> list_cmd = new ArrayList<String>();
			list_cmd.add("git add .");
			list_cmd.add("git commit -m='hello' --section");
			list_cmd.add("git commit -m='world'");

			CLIClient client = CLIUtils.getClient(context);
			for (String cmd : list_cmd) {
				client.execute(context, cmd);
			}

		} finally {
			tester.close(testing);
		}

	}

}
