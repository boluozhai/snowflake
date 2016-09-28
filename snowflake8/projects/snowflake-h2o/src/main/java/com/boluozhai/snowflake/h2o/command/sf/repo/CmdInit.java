package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.site.RepositoryType;
import com.boluozhai.snowflake.xgit.site.SiteRepository;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;

public class CmdInit extends AbstractCLICommandHandler {

	private static class ConfigBuilder {

		private final Config conf;

		public ConfigBuilder(Config config) {
			this.conf = config;
		}

		public void set_enable() {
			String key = Config.xgit.enable;
			String value = String.valueOf(true);
			conf.setProperty(key, value);
		}

		public void set_hash_algorithm(String value) {
			String key = Config.xgit.hashalgorithm;
			conf.setProperty(key, value);
		}

		public void set_hash_path_pattern(String value) {
			String key = Config.xgit.hashpathpattern;
			conf.setProperty(key, value);
		}

		public void set_type(String type) {
			String key = Config.xgit.siterepositorytype;
			conf.setProperty(key, type);
		}

	}

	private static class Inner {

		private final SnowflakeContext context;

		public String repo_name;
		public boolean bare;
		private String type = RepositoryType.normal;

		private String hash_path_pattern;

		private String hash_algorithm;

		public Inner(SnowflakeContext context) {
			this.context = context;
		}

		public void set_as_site_repo() {

			// location of the repo
			CurrentLocation cur_loc = CurrentLocation.Factory.get(context);
			URI loc = cur_loc.getLocation(context);
			VFS vfs = VFS.Factory.getVFS(context);
			VFile file = vfs.newFile(loc);
			file = file.child(this.repo_name);
			loc = file.toURI();

			// open repo
			RepositoryManager rm = XGit.getRepositoryManager(context);
			Repository repo = rm.open(context, loc, null);
			Config config = repo.context().getBean(
					XGitContext.component.config, Config.class);

			// set config
			ConfigBuilder cb = new ConfigBuilder(config);

			cb.set_enable();
			cb.set_type(this.type);
			cb.set_hash_path_pattern(this.hash_path_pattern);
			cb.set_hash_algorithm(this.hash_algorithm);

			try {
				config.save();
			} catch (IOException e) {
				throw new SnowflakeException(e);
			}

		}

		public void run_git_init() {

			String bare = this.bare ? "--bare" : "";
			String name = this.repo_name;
			String command = String.format("git init %s %s", bare, name);

			CLIClient client = CLIUtils.getClient(context);
			client.execute(context, command);

		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--bare");
			builder.option("--type", "value");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			this.bare = ps.hasOption("--bare");
			this.type = ps.getRequiredOption("--type");
			this.repo_name = ps.getRequiredArgument(0);

			this.check_type();
			this.check_repo_name();

		}

		private void check_repo_name() {
			String value = this.repo_name;
			char[] bad_ch = { '\\', '/', ' ' };
			for (char ch : bad_ch) {
				if (value.indexOf(ch) < 0) {
					continue;
				} else {
					String msg = "the repo name [%s] contain forbidden char: %d";
					String.format(msg, value, ch);
					throw new SnowflakeException(msg);
				}
			}
		}

		private void check_type() {
			String[] accept = { SiteRepository.TYPE.user,
					SiteRepository.TYPE.data, };
			String value = this.type;
			for (String reg : accept) {
				if (reg.equals(value)) {
					return;
				}
			}
			String msg = "the repo type [%s] is not accepted.";
			msg = String.format(msg, value);
			throw new SnowflakeException(msg);
		}

		public void load_site_system_repo() {

			XGitSite site = XGitSite.Agent.getSite(context);
			SystemRepository sys_repo = site.getSystemRepository();
			ComponentContext cc = sys_repo.getComponentContext();
			Config conf = (Config) cc
					.getAttribute(XGitContext.component.config);

			String path_pattern = conf.getProperty(Config.xgit.hashpathpattern);
			String hash_algorithm = conf.getProperty(Config.xgit.hashalgorithm);
			String repo_type = conf.getProperty(Config.xgit.siterepositorytype);
			String enable = conf.getProperty(Config.xgit.enable);

			this.check_text(enable, "true");
			this.check_text(repo_type, SiteRepository.TYPE.system);

			this.hash_path_pattern = path_pattern;
			this.hash_algorithm = hash_algorithm;

		}

		private void check_text(String todo, String reg) {
			if (!reg.equals(todo)) {
				String msg = "the value [%s] not match regular [%s].";
				msg = String.format(msg, todo, reg);
				throw new SnowflakeException(msg);
			}
		}

	}

	@Override
	public void process(SnowflakeContext context, String command) {
		Inner inner = new Inner(context);
		inner.load_param();
		inner.load_site_system_repo();
		inner.run_git_init();
		inner.set_as_site_repo();
	}

}
