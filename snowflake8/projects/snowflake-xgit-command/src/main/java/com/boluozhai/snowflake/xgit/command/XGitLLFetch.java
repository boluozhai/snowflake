package com.boluozhai.snowflake.xgit.command;

import java.io.PrintStream;
import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.fetch.SmartFetchModel;
import com.boluozhai.snowflake.xgit.remotes.Remote;
import com.boluozhai.snowflake.xgit.remotes.RemoteManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class XGitLLFetch extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		try {

			CLIResponse cli = CLIResponse.Agent.getResponse(context);
			PrintStream out = cli.out();

			InnerTask task = new InnerTask(context);

			task.load_param();
			task.load_local_repo();
			task.load_remote_repo();

			task.do_fetch();

			out.println("done.");

			// } catch (IOException e) {
			// throw new SnowflakeException(e);

		} finally {
		}

	}

	private static class InnerTask {

		private final SnowflakeContext context;

		private ObjectId param_want;
		private ObjectId param_have;
		private String param_remote;

		private Repository local_repo;
		private GitHttpRepo remote_repo;

		public InnerTask(SnowflakeContext context) {
			this.context = context;
		}

		public void load_local_repo() {
			RepositoryAgent agent = RepositoryAgent.Factory.get(context);
			this.local_repo = agent.getRepository(context);
		}

		public void load_remote_repo() {

			RemoteManager rm = RemoteManager.Factory.getInstance(local_repo);
			Remote remote = rm.get(this.param_remote);
			if (remote == null) {
				String msg = "cannot find remote config by name : %s";
				msg = String.format(msg, this.param_remote);
				throw new SnowflakeException(msg);
			}

			URI uri = URI.create(remote.getUrl());

			GitHttpClient client = GitHttpClient.Factory.getInstance(context);
			this.remote_repo = client.connect(uri);

		}

		public void do_fetch() {

			Repository local = this.local_repo;
			GitHttpRepo remote = this.remote_repo;
			ObjectId from = this.param_want;
			ObjectId to = this.param_have;

			SmartFetchModel model = new SmartFetchModel(local, remote, from, to);
			model.fire();

		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--remote", "");
			builder.option("--want", "");
			builder.option("--have", "");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			// if the begin is a commit, the end is needed.
			String remote = ps.getRequiredOption("--remote");
			String want = ps.getRequiredOption("--want");
			String have = ps.getOption("--have");

			if (have != null) {
				this.param_have = ObjectId.Factory.create(have);
			}

			if (want != null) {
				this.param_want = ObjectId.Factory.create(want);
			}

			this.param_remote = remote;

		}
	}

}
