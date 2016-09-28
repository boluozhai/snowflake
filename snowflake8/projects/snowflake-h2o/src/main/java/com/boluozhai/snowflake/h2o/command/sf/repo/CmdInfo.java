package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.cli.util.ParamSet;
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

		Inner inner = new Inner(context, this);
		inner.load_param();
		inner.execute();

	}

	private static class Inner {

		private final PrintStream out;
		private final SnowflakeContext context;
		private final String usage;

		private String _a;
		private String _c;
		private String _e;
		private String _pa;
		private String _pr;

		public Inner(SnowflakeContext context, CmdInfo parent) {
			CLIResponse resp = CLIResponse.Agent.getResponse(context);
			this.out = resp.out();
			this.context = context;
			this.usage = parent.getUsage();
		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();

			builder.option("--attr");
			builder.option("--conf");
			builder.option("--env");
			builder.option("--param");
			builder.option("--prop");

			builder.option("-a");
			builder.option("-c");
			builder.option("-e");
			builder.option("-pa");
			builder.option("-pr");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			this._a = this.load_param_value(ps, "-a", "--attr");
			this._c = this.load_param_value(ps, "-c", "--conf");
			this._e = this.load_param_value(ps, "-e", "--env");
			this._pa = this.load_param_value(ps, "-pa", "--param");
			this._pr = this.load_param_value(ps, "-pr", "--prop");

		}

		private String load_param_value(ParamSet ps, String k1, String k2) {
			Parameter p1 = ps.getOptionParam(k1);
			Parameter p2 = ps.getOptionParam(k2);
			if (p1 != null) {
				return p1.toString();
			} else if (p2 != null) {
				return p2.toString();
			} else {
				return null;
			}
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

			if (this.isAllParamIsNull()) {
				this.printUsage();
			}

		}

		private void printUsage() {
			this.out.println("Usage : " + this.usage);
		}

		private boolean isAllParamIsNull() {
			boolean result = false;
			if (_a != null) {
			} else if (_c != null) {
			} else if (_e != null) {
			} else if (_pa != null) {
			} else if (_pr != null) {
			} else {
				result = true;
			}
			return result;
		}

		private void printLocation(ComponentContext cc) {

			out.println("[Location]");
			out.println("  " + cc.getURI());

		}

		private void printConfig(ComponentContext cc) {

			if (this._c == null) {
				return;
			}

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

			if (this._e == null) {
				return;
			}

			out.println("[Environments]");
			Map<String, String> map = SnowflakeEnvironments.MapGetter
					.getMap(cc);
			this.printMap(map);

		}

		private void printProperty(ComponentContext cc) {

			if (this._pr == null) {
				return;
			}

			out.println("[Properties]");
			Map<String, String> map = SnowflakeProperties.MapGetter.getMap(cc);
			this.printMap(map);

		}

		private void printParameter(ComponentContext cc) {

			if (this._pa == null) {
				return;
			}

			out.println("[Parameters]");
			Map<String, String> map = SnowflakeParameters.MapGetter.getMap(cc);
			this.printMap(map);

		}

		private void printAttribute(ComponentContext cc) {

			if (this._a == null) {
				return;
			}

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
