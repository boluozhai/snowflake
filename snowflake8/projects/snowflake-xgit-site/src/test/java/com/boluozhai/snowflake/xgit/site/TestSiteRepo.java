package com.boluozhai.snowflake.xgit.site;

import java.net.URI;
import java.util.Arrays;

import org.junit.Test;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.XGitContext;

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
			XGitContext cc = sys_repo.context();

			URI uri = sys_repo.context().getURI();
			System.out.println("sys.repo.uri = " + uri);

			sys_repo = cc.getBean(XGitSiteContext.component.system_repo,
					SystemRepository.class);

			this.list_all_components(cc);

		} finally {
			tester.close(testing);
		}

	}

	private void list_all_components(XGitContext cc) {

		SnowflakeContext parent = cc.getParent();
		String[] keys = cc.getAttributeNames();
		Arrays.sort(keys);

		System.out.println("component-list:");

		for (String key : keys) {
			Object v1 = parent.getAttribute(key, null);
			Object v2 = cc.getAttribute(key, null);
			String val = null;
			if (v1 == null) {
				val = v2.getClass().getName();
			} else {
				val = "";
				key = "  " + key;
			}
			System.out.format("  %s = %s\n", key, val);
		}

	}
}
