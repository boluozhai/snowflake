package com.boluozhai.snowflake.xgit.http.client;

import java.net.URI;

import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;

public class GitHttpDriverTest {

	// @Test

	public void test() {

		String href = "https://github.com/git/git.git";

		Tester tester = null;
		Testing testing = null;
		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			RepositoryManager rm = XGit.getRepositoryManager(context);
			URI uri = URI.create(href);
			RepositoryOption option = null;
			Repository repo = rm.open(context, uri, option);

			XGitContext xgc = repo.context();
			xgc.getBean(XGitContext.component.refs);
			xgc.getBean(XGitContext.component.objects);
			xgc.getBean(XGitContext.component.client);
			xgc.getBean(XGitContext.component.config);

		} finally {
			tester.close(testing);
		}

	}

}
