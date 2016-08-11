package com.boluozhai.snowflake.libwebapp.update;

import org.junit.Test;

import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.libwebapp.update.publisher.PublisherKit;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;

public class TestPublisherPub {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();
			CLIClient cli = CLIUtils.getClient(context);

			cli.execute(context, "snow-publish-add");
			cli.execute(context, "snow-publish-commit");
			cli.execute(context, "snow-publish-push");

			PublisherKit pubkit = PublisherKit.Agent.getInstance(context);
			FileRepository repo = pubkit.getRepository();
			System.out.println("publisher.repository = " + repo.getFile());

		} finally {
			tester.close(testing);
		}

	}

}
