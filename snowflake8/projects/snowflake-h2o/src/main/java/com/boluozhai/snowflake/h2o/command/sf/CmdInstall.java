package com.boluozhai.snowflake.h2o.command.sf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.Properties;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.site.RepositoryType;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;

public class CmdInstall extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		try {

			InnerTask task = new InnerTask(context);
			task.load_param();
			task.load_system_repo();
			ObjectId desc_id = task.get_descriptor();
			if (desc_id == null) {
				task.make_descriptor();
			} else {
				String msg = "the current system repo has a descriptor: "
						+ desc_id;
				throw new SnowflakeException(msg);
			}

			CLIResponse cli = CLIResponse.Agent.getResponse(context);
			PrintStream out = cli.out();
			out.println("install system repo at "
					+ task.getSystemRepoLocation());
			out.println("done.");

		} catch (IOException e) {

			throw new SnowflakeException(e);

		} finally {
		}

	}

	private final static String regular_descriptor_ref_name = "xgit.private_refs/repository/descriptor";

	private static class InnerTask {

		private final SnowflakeContext context;
		private SystemRepository sys_repo;
		private RefManager refs;
		private Config config;
		private ObjectBank bank;

		public InnerTask(SnowflakeContext context) {
			this.context = context;
		}

		public String getSystemRepoLocation() {
			return this.sys_repo.getComponentContext().getURI().toString();
		}

		public void load_param() {
			// TODO Auto-generated method stub

		}

		public ObjectId get_descriptor() {
			final String key = Config.xgit.siterepositorydescriptor;
			String refname = config.getProperty(key, null);
			if (refname == null) {
				return null;
			}
			Ref ref = refs.getReference(refname);
			if (!ref.exists()) {
				return null;
			}
			return ref.getId();
		}

		public void load_system_repo() {
			XGitSite site = XGitSite.Agent.getSite(context);
			this.sys_repo = site.getSystemRepository();
			this.refs = RefManager.Factory.getInstance(sys_repo);
			this.config = Config.Factory.getInstance(sys_repo);
			this.bank = ObjectBank.Factory.getBank(sys_repo);
		}

		public void make_descriptor() throws IOException {

			final String desc_refname = regular_descriptor_ref_name;
			final String obj_type = GitObject.TYPE.descriptor;
			final String repo_type = RepositoryType.system;

			URI location = this.sys_repo.getComponentContext().getURI();

			// make prop

			Properties props = new Properties();
			props.setProperty("timestamp",
					String.valueOf(System.currentTimeMillis()));
			props.setProperty("location", location.toString());
			props.setProperty(Config.xgit.siterepositorytype, repo_type);

			// create object

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			props.store(baos, obj_type);
			byte[] ba = baos.toByteArray();

			GitObjectBuilder builder = bank.newBuilder(obj_type, ba.length);
			builder.write(ba, 0, ba.length);
			GitObject gitobj = builder.create();
			ObjectId desc_id = gitobj.id();

			// update refs

			Ref ref = this.refs.getReference(desc_refname);
			ref.setId(desc_id);

			// save to config

			config.setProperty(Config.xgit.enable, "true");
			config.setProperty(Config.xgit.hashalgorithm, "sha-1");
			config.setProperty(Config.xgit.hashpathpattern, "xx/xxxx");
			config.setProperty(Config.xgit.siterepositorydescriptor,
					ref.getName());
			config.setProperty(Config.xgit.siterepositorytype, repo_type);

			config.save();

		}
	}

}
