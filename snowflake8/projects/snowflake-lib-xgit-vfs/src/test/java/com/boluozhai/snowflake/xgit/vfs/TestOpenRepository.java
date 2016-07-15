package com.boluozhai.snowflake.xgit.vfs;

import java.io.File;
import java.net.URI;

import org.junit.Test;

import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;

public class TestOpenRepository {

	public void test_repo(TestContext context, String offset_path) {

		File base = context.getWorkingPath();
		URI uri = (new File(base, offset_path)).toURI();

		RepositoryManager repo_man = XGit.getRepositoryManager(context);
		FileRepository repo = (FileRepository) repo_man
				.open(context, uri, null);
		ComponentContext repo_context = repo.getComponentContext();

		FileConfig config = repo_context.getBean(XGitContext.component.config,
				FileConfig.class);

		FileObjectBank objects = repo_context.getBean(
				XGitContext.component.objects, FileObjectBank.class);

		FileWorkspace working = repo_context.getBean(
				XGitContext.component.workspace, FileWorkspace.class);

		VFile repo_path = repo.getFile();
		System.out.format("find repo in path of %s\n", uri);
		System.out.format("open a file-Git-Repo at %s\n", repo_path);

		this.log(working);
		this.log(repo);
		this.log(config);
		this.log(objects);

	}

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;
		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			this.test_repo(context, "repos/repo1_normal/abc");
			this.test_repo(context, "repos/repo1_normal/.snow/objects");
			this.test_repo(context, "repos/repo2_bare/repo.snow/objects");

		} finally {
			tester.close(testing);
		}

	}

	private void log(FileXGitComponent com) {

		String a = com.getClass().getName();
		String b = com.getFile().toURI().toString();
		System.out.format("  %100s = %s\n", a, b);

	}
}
