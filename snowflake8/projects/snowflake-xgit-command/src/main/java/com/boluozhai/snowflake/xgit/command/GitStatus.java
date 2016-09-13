package com.boluozhai.snowflake.xgit.command;

import java.io.PrintStream;
import java.net.URI;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class GitStatus extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {
		// TODO Auto-generated method stub

		ParamReader.Builder prb = ParamReader.newBuilder();
		prb.option("--op-0");
		prb.option("--op-1", "1");
		prb.option("--op-2", 2);
		ParamReader reader = prb.create(context);
		System.out.println("Parameters:");
		for (;;) {
			Parameter p = reader.read();
			if (p == null) {
				break;
			} else {
				// System.out.println("  " + p);
			}
		}

		RepositoryAgent agent = RepositoryAgent.Factory.get(context);
		Repository repo = agent.getRepository(context);
		URI loc = repo.getComponentContext().getURI();

		CLIResponse resp = CLIResponse.Agent.getResponse(context);
		PrintStream out = resp.out();
		out.format("TODO: status of repo [%s]", loc);

	}

}
