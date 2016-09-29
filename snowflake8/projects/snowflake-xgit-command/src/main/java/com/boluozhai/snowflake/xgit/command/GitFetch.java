package com.boluozhai.snowflake.xgit.command;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.remotes.Remote;
import com.boluozhai.snowflake.xgit.remotes.RemoteManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class GitFetch extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {
		// TODO Auto-generated method stub

		InnerTask task = new InnerTask(context);
		task.load_param();
		task.load_local_repo();
		task.load_remote_agent();

		ObjectId base_commit = task.load_base_commit_id();
		ObjectId head_commit = task.load_head_commit_id();

	}

	private static class InnerTask {

		private final SnowflakeContext context;
		private Repository local_repo;
		private String param_remote_name;
		private String param_ref_name;

		public InnerTask(SnowflakeContext context) {
			this.context = context;
		}

		public ObjectId load_head_commit_id() {
			// TODO Auto-generated method stub
			return null;
		}

		public ObjectId load_base_commit_id() {
			// TODO Auto-generated method stub
			return null;
		}

		public void load_remote_agent() {
			// TODO Auto-generated method stub

			RemoteManager rm = RemoteManager.Factory.getInstance(local_repo);

			Remote remote = rm.get(this.param_remote_name);

			System.out.println("load remote agent for " + remote.getUrl());

		}

		public void load_local_repo() {
			RepositoryAgent agent = RepositoryAgent.Factory.get(context);
			Repository repo = agent.getRepository(context);
			this.local_repo = repo;
		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--all", "");
			builder.option("--multiple", "");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			this.param_remote_name = ps.getArgument(0, null);
			this.param_ref_name = ps.getArgument(1, null);

		}
	}

}
