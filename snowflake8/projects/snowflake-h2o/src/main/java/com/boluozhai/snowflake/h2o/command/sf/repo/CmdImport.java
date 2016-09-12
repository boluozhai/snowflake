package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.PrintStream;
import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.RepositoryType;
import com.boluozhai.snowflake.xgit.site.SiteRepositoryManager;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.site.pojo.SiteRepoInfo;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;

public class CmdImport extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		Inner inner = new Inner(context);

		inner.loadParam();
		inner.loadCurrentRepo();
		inner.loadSystemRepo();

		inner.check_current_repo_state();
		inner.make_descriptor();
		inner.do_import();

	}

	private static class Helper {

		public static String getType(Repository repo) {
			Config conf = (Config) repo.getComponentContext().getBean(
					XGitContext.component.config);
			return conf.getProperty(Config.xgit.siterepositorytype);
		}

	}

	private static class Inner {

		private final SnowflakeContext context;
		private SystemRepository system_repo;
		private Repository current_repo;
		private String param_type;

		public Inner(SnowflakeContext context) {
			this.context = context;
		}

		public void check_current_repo_state() {

			Repository repo = this.current_repo;
			Config conf = (Config) repo.getComponentContext().getBean(
					XGitContext.component.config);
			String type = conf.getProperty(Config.xgit.siterepositorytype);
			String descriptor = conf.getProperty(
					Config.xgit.siterepositorydescriptor, null);

			String reg_type = RepositoryType.normal;
			if (!type.equals(reg_type)) {
				String msg = "The current repo MUST be with a type of '%s'.";
				msg = String.format(msg, reg_type);
				throw new RuntimeException(msg);
			}

			if (descriptor != null) {
				String msg = "The current repo has a descriptor.";
				throw new RuntimeException(msg);
			}

		}

		public void make_descriptor() {
			// TODO Auto-generated method stub

		}

		public void loadParam() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--type", "value");

			ParamReader reader = builder.create(context);
			for (;;) {
				Parameter p = reader.read();
				if (p == null) {
					break;
				} else {
					this.onParam(p);
				}
			}

			if (this.param_type == null) {
				throw new RuntimeException("");
			}

		}

		private void onParam(Parameter p) {
			if (p.isOption) {
				String name = p.name;
				if (name == null) {
					// NOP
				} else if (name.equals("--type")) {
					this.param_type = p.value;
				} else {
					// NOP
				}
			} else {
			}
		}

		public void do_import() {
			// TODO Auto-generated method stub

			CLIResponse resp = CLIResponse.Agent.getResponse(context);
			PrintStream out = resp.out();

			URI sys_loc = this.system_repo.getComponentContext().getURI();
			URI cur_loc = this.current_repo.getComponentContext().getURI();

			// log
			out.println("Import repo[CURRENT] to the site[SYSTEM].");
			out.format("  SYSTEM  = %s\n", sys_loc);
			out.format("  CURRENT = %s\n", cur_loc);

			// make info

			SiteRepoInfo info = new SiteRepoInfo();
			info.setDescriptor("");
			info.setId("");
			info.setUri(cur_loc.toString());
			info.setType(Helper.getType(current_repo));

			SiteRepositoryManager srm = this.system_repo.getRepositoryManager();
			srm.insert(info);

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
