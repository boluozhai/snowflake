package com.boluozhai.snowflake.xgit.site;

import java.net.URI;

import org.junit.Test;

import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class TestSiteRepo {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			XGitSite site = XGitSite.Agent.getSite(context);
			SystemRepository sys_repo = site.getSystemRepository();

			URI uri = sys_repo.context().getURI();
			System.out.println("sys.repo.uri = " + uri);

		} finally {
			tester.close(testing);
		}

	}

}
