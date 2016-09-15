package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.SnowflakeEnvironments;
import com.boluozhai.snowflake.context.SnowflakeParameters;
import com.boluozhai.snowflake.context.SnowflakeProperties;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class CmdInfo extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		Inner inner = new Inner(context);
		inner.execute();

	}

	private static class Inner {

		private final PrintStream out;
		private final SnowflakeContext context;

		public Inner(SnowflakeContext context) {
			CLIResponse resp = CLIResponse.Agent.getResponse(context);
			this.out = resp.out();
			this.context = context;
		}

		public void execute() {

			Repository repo = this.getRepo();
			ComponentContext cc = repo.getComponentContext();
			this.printLocation(cc);
			this.printConfig(cc);
			this.printParameter(cc);
			this.printProperty(cc);
			this.printEnvironment(cc);
			this.printAttribute(cc);

		}

		private void printLocation(ComponentContext cc) {

			out.println("[Location]");
			out.println("  " + cc.getURI());

		}

		private void printConfig(ComponentContext cc) {

			out.println("[Config]");
			Config conf = (Config) cc
					.getAttribute(XGitContext.component.config);
			Map<String, String> map = SnowflakeProperties.MapGetter
					.getMap(conf);
			this.printMap(map);

		}

		private void printMap(Map<String, String> map) {

			List<String> keys = new ArrayList<String>(map.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				String value = map.get(key);
				out.format("  %30s = %s\n", key, value);
			}

		}

		private void printEnvironment(ComponentContext cc) {

			out.println("[Environments]");
			Map<String, String> map = SnowflakeEnvironments.MapGetter
					.getMap(cc);
			this.printMap(map);

		}

		private void printProperty(ComponentContext cc) {

			out.println("[Properties]");
			Map<String, String> map = SnowflakeProperties.MapGetter.getMap(cc);
			this.printMap(map);

		}

		private void printParameter(ComponentContext cc) {

			out.println("[Parameters]");
			Map<String, String> map = SnowflakeParameters.MapGetter.getMap(cc);
			this.printMap(map);

		}

		private void printAttribute(ComponentContext cc) {

			out.println("[Attributes]");

			Map<String, String> map = new HashMap<String, String>();
			for (String key : cc.getAttributeNames()) {
				Object obj = cc.getAttribute(key);
				String val = obj.getClass().getName();
				map.put(key, val);
			}
			this.printMap(map);

		}

		private Repository getRepo() {

			RepositoryAgent agent = RepositoryAgent.Factory.get(context);
			return agent.getRepository(context);

		}
	}

}
