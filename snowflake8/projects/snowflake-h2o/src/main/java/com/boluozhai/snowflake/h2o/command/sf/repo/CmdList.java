package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.site.RepositoryType;
import com.boluozhai.snowflake.xgit.site.SiteRepositoryManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.site.pojo.SiteRepoInfo;

public class CmdList extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		Inner inner = new Inner(context);
		inner.loadRepoInfo();
		inner.printInfo();

	}

	private static class Inner {

		private final SnowflakeContext context;
		private final RepoGroupSet repos = new RepoGroupSet();
		private SystemRepository system;

		public Inner(SnowflakeContext context) {
			this.context = context;
		}

		public void printInfo() {

			CLIResponse resp = CLIResponse.Agent.getResponse(context);
			PrintStream out = resp.out();
			this.printSystemInfo(out);
			repos.printSelf(out);

		}

		private void printSystemInfo(PrintStream out) {
			// TODO Auto-generated method stub

			URI loc = system.getComponentContext().getURI();

			out.println("[system]");
			out.println("  location = " + loc);

		}

		public void loadRepoInfo() {

			XGitSite site = XGitSite.Agent.getSite(context);
			SystemRepository sys_repo = site.getSystemRepository();
			SiteRepositoryManager srm = sys_repo.getRepositoryManager();

			String[] ids = srm.ids();
			for (String id : ids) {
				SiteRepoInfo info = srm.get(id);
				this.onLoadRepoInfo(info);
			}

			repos.getGroup(RepositoryType.data);
			repos.getGroup(RepositoryType.normal);
			repos.getGroup(RepositoryType.partition);
			repos.getGroup(RepositoryType.system);
			repos.getGroup(RepositoryType.user);

			this.system = sys_repo;

		}

		private void onLoadRepoInfo(SiteRepoInfo info) {
			String type = info.getType();
			RepoGroup group = repos.getGroup(type);
			group.addInfo(info);
		}
	}

	private static class RepoGroup {

		private final String type;
		private final Map<String, SiteRepoInfo> table;

		public RepoGroup(String aType) {
			this.type = aType;
			this.table = new HashMap<String, SiteRepoInfo>();
		}

		public void addInfo(SiteRepoInfo info) {
			String id = info.getId();
			table.put(id, info);
		}

		public void printSelf(PrintStream out) {
			out.format("[%s]\n", type);
			List<String> keys = new ArrayList<String>(table.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				SiteRepoInfo info = table.get(key);
				this.printInfo(info, out);
			}
		}

		private void printInfo(SiteRepoInfo info, PrintStream out) {
			// TODO Auto-generated method stub

			String id = info.getId();
			out.format("  %s\n", id);

		}

	}

	private static class RepoGroupSet {

		private final Map<String, RepoGroup> table;

		public RepoGroupSet() {
			this.table = new HashMap<String, RepoGroup>();
		}

		public void printSelf(PrintStream out) {
			List<String> keys = new ArrayList<String>(table.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				RepoGroup group = table.get(key);
				String type = group.type;
				if (RepositoryType.system.equals(type)) {
					continue;
				} else {
					group.printSelf(out);
				}
			}
		}

		public RepoGroup getGroup(String type) {
			RepoGroup group = table.get(type);
			if (group == null) {
				group = new RepoGroup(type);
				table.put(type, group);
			}
			return group;
		}
	}

}
