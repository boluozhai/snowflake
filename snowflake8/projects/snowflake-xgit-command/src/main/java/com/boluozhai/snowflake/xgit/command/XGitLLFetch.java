package com.boluozhai.snowflake.xgit.command;

import java.io.PrintStream;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class XGitLLFetch extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		try {

			CLIResponse cli = CLIResponse.Agent.getResponse(context);
			PrintStream out = cli.out();

			InnerTask task = new InnerTask(context);
			task.load_param();

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
		private SmartClient smart_client;

		public InnerTask(SnowflakeContext context) {
			this.context = context;
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
