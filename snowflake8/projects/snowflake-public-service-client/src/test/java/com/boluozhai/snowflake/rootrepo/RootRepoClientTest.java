package com.boluozhai.snowflake.rootrepo;

import java.io.IOException;

import org.junit.Test;

import com.boluozhai.snowflake.rootrepo.method.DoTest;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class RootRepoClientTest {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			RootRepoClient client = RootRepoClient.Agent.getClient(context);
			DoTest the_test = client.doTest();
			the_test.play();

		} catch (IOException e) {
			throw new RuntimeException(e);

		} finally {
			tester.close(testing);
		}

	}

}
