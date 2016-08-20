package com.boluozhai.snowflake.xgit.vfs;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.boluozhai.snowflake.context.MutableProperties;
import com.boluozhai.snowflake.context.SnowflakeProperties;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class TestRepoConfigLS {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;
		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			Config config1 = this.openRepoConfig(context, "repo1");
			Config config2 = this.openRepoConfig(context, "repo2");

			config1.load();

			Map<String, String> map = SnowflakeProperties.MapGetter.getMap(config1);
			this.setProperties(config2, map);
			config2.setProperty(this.getClass().getName(), "for-test");

			config2.save();

			this.logProperties("config2:", config2);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			tester.close(testing);
		}

	}

	private void logProperties(String tag, SnowflakeProperties kv) {

		System.out.println(tag);

		String fmt = "    %50s = %s\n";
		String[] keys = kv.getPropertyNames();
		for (String key : keys) {
			String value = kv.getProperty(key);
			System.out.format(fmt, key, value);
		}
	}

	private void setProperties(MutableProperties target,
			Map<String, String> data) {
		Set<String> keys = data.keySet();
		for (String key : keys) {
			String value = data.get(key);
			target.setProperty(key, value);
		}
	}

	private Config openRepoConfig(TestContext context, String path) {

		File wk = context.getWorkingPath();
		URI uri = (new File(wk, path)).toURI();
		RepositoryManager xgit = XGit.getRepositoryManager(context);
		Repository repo = xgit.open(context, uri, null);
		return XGitContext.GET.config(repo.context());

	}

}
