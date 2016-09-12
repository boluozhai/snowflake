package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.RepositoryType;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;

public class CmdInit extends AbstractCLICommandHandler {

	private class myGitInitParam {

		private final SnowflakeContext context;
		public String repo_name;
		public boolean bare;
		private String type = RepositoryType.normal;

		public myGitInitParam(SnowflakeContext context) {
			this.context = context;
		}

		public void setAsSiteRepo() {
			// TODO Auto-generated method stub

			CurrentLocation cur_loc = CurrentLocation.Factory.get(context);
			URI loc = cur_loc.getLocation(context);
			VFS vfs = VFS.Factory.getVFS(context);
			VFile file = vfs.newFile(loc);
			file = file.child(this.repo_name);
			loc = file.toURI();

			RepositoryManager rm = XGit.getRepositoryManager(context);
			Repository repo = rm.open(context, loc, null);
			Config config = repo.context().getBean(
					XGitContext.component.config, Config.class);

			String key, value;

			// enable
			key = Config.xgit.enable;
			value = String.valueOf(true);
			config.setProperty(key, value);

			// repo_type
			key = Config.xgit.siterepositorytype;
			value = this.type;
			config.setProperty(key, value);

			try {
				config.save();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void runGitInit() {

			String bare = this.bare ? "--bare" : "";
			String name = this.repo_name;
			String command = String.format("git init %s %s", bare, name);

			CLIClient client = CLIUtils.getClient(context);
			client.execute(context, command);

		}

		public void onParam(Parameter p) {
			if (p.isOption) {
				String key = p.name;
				if (key == null) {
					// NOP
				} else if (key.equals("--bare")) {
					this.bare = true;
				} else if (key.equals("--type")) {
					this.type = p.value;
				}
			} else {
				if (this.repo_name == null) {
					this.repo_name = p.value;
				}
			}
		}

		public void loadParameters() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--bare");
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

		}

	}

	@Override
	public void process(SnowflakeContext context, String command) {
		myGitInitParam inner = new myGitInitParam(context);
		inner.loadParameters();
		inner.runGitInit();
		inner.setAsSiteRepo();
	}

}
