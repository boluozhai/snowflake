package com.boluozhai.snowflake.xgit.vfs;

import java.net.URI;

import org.junit.Test;

import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snow.vfs.VFile;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.RepositoryContext;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class TestOpenRepository {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;
		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			URI uri = context.getWorkingPath().toURI();

			RepositoryManager repo_man = XGit.getRepositoryManager(context);
			FileRepository repo = (FileRepository) repo_man.open(context, uri,
					null);
			ComponentContext repo_context = repo.getComponentContext();

			FileConfig config = repo_context.getBean(
					RepositoryContext.component.config, FileConfig.class);

			FileObjectBank objects = repo_context.getBean(
					RepositoryContext.component.objects, FileObjectBank.class);

			VFile repo_path = repo.getFile();
			System.out.format("open a file-Git-Repo at %s\n", repo_path);

		} finally {
			tester.close(testing);
		}

	}
}
