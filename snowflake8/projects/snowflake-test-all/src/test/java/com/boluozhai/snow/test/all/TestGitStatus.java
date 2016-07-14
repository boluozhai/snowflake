package com.boluozhai.snow.test.all;

import java.io.File;
import java.net.URI;

import org.junit.Test;

import com.boluozhai.snow.cli.CLIUtils;
import com.boluozhai.snow.cli.client.CLIClient;
import com.boluozhai.snow.cli.client.CLIProcess;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class TestGitStatus {

	@Test
	public void test() {

		Testing testing = null;
		Tester tester = Tester.Factory.newInstance();

		try {
			testing = tester.open(this);
			test_git_status(testing.context());
		} finally {
			tester.close(testing);
		}

	}

	public void test_git_status(TestContext tc) {

		File path = tc.getWorkingPath();
		path = new File(path, "a/b/c");
		URI uri = path.toURI();

		SnowContext context = SnowContextUtils.getContext();
		RepositoryManager xgit_man = XGit.getRepositoryManager(context);
		Repository repo = xgit_man.open(context, uri, null);
		XGitContext repo_context = repo.context();

		CLIClient cli = CLIUtils.getClient(repo_context);
		CLIProcess pro = cli.execute(repo_context, "git status");
		pro.run();

	}

}
