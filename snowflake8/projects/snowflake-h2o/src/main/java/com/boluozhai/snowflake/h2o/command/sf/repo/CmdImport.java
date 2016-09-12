package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.PrintStream;
import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;

public class CmdImport extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		Inner inner = new Inner(context);

		inner.loadCurrentRepo();
		inner.loadSystemRepo();

		inner.do_import();

	}

	private class Inner {

		private final SnowflakeContext context;
		private SystemRepository system_repo;
		private Repository current_repo;

		public Inner(SnowflakeContext context) {
			this.context = context;
		}

		public void do_import() {
			// TODO Auto-generated method stub

			CLIResponse resp = CLIResponse.Agent.getResponse(context);
			PrintStream out = resp.out();

			URI sys = this.system_repo.getComponentContext().getURI();
			URI cur = this.current_repo.getComponentContext().getURI();
			out.println("Import repo[CURRENT] to the site[SYSTEM].");
			out.format("  SYSTEM  = %s\n", sys);
			out.format("  CURRENT = %s\n", cur);

		}

		public void loadCurrentRepo() {

			CurrentLocation cl = CurrentLocation.Factory.get(context);
			URI loc = cl.getLocation(context);
			RepositoryManager xgit = XGit.getRepositoryManager(context);
			this.current_repo = xgit.open(context, loc, null);

		}

		public void loadSystemRepo() {

			XGitSite site = XGitSite.Agent.getSite(context);
			this.system_repo = site.getSystemRepository();

		}

	}

}
