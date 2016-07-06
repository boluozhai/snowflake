package com.boluozhai.snow.test.all;

import java.io.File;
import java.net.URI;

import org.junit.Test;

import com.boluozhai.snow.cli.CLIUtils;
import com.boluozhai.snow.cli.client.CLIClient;
import com.boluozhai.snow.cli.client.CLIProcess;
import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.context.utils.SnowAppContextUtils;
import com.boluozhai.snow.test.TestContext;
import com.boluozhai.snow.test.Tester;
import com.boluozhai.snow.test.Testing;
import com.boluozhai.snow.test.support.DefaultTester;
import com.boluozhai.snow.xgit.XGit;
import com.boluozhai.snow.xgit.repository.Repository;
import com.boluozhai.snow.xgit.repository.RepositoryContext;
import com.boluozhai.snow.xgit.repository.RepositoryManager;

public class TestGitStatus {

	@Test
	public void test() {

		Testing testing = null;
		Tester tester = new DefaultTester();

		try {
			testing = tester.open(this);
			test_git_status(testing.context());
		} finally {
			tester.close(testing);
		}

	}

	public void test_git_status(TestContext tc) {

		File path = tc.workingPath();
		path = new File(path, "a/b/c");
		URI uri = path.toURI();

		SnowContext context = SnowAppContextUtils.getContext();
		RepositoryManager xgit_man = XGit.getRepositoryManager(context);
		Repository repo = xgit_man.open(context, uri, null);
		RepositoryContext repo_context = repo.context();

		CLIClient cli = CLIUtils.getClient(repo_context);
		CLIProcess pro = cli.execute(repo_context, "git status");
		pro.run();

	}

}
