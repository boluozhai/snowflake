package com.boluozhai.snowflake.h2o.command.sf.user;

import java.io.PrintStream;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataLine;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.pojo.Account;
import com.boluozhai.snowflake.util.IOTools;

public class CmdAdd extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		Inner inner = new Inner(context);

		inner.load_param();
		inner.print_info();
		inner.exec();

	}

	private static class Inner {

		private final SnowflakeContext context;
		private final PrintStream out;

		private String email;
		private String nickname;

		public Inner(SnowflakeContext context) {

			CLIResponse resp = CLIResponse.Agent.getResponse(context);

			this.out = resp.out();
			this.context = context;

		}

		public void print_info() {
			out.println("create user");
			out.println("     email = " + email);
			out.println("  nickname = " + nickname);
			out.println();
		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--email", "value");
			builder.option("--nickname", "value");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			this.email = ps.getRequiredOption("--email");
			this.nickname = ps.getOption("--nickname");

		}

		public void exec() {

			DataClient dc = null;
			Transaction tx = null;

			try {
				dc = H2oDataTable.openClient(context);
				tx = dc.beginTransaction();

				Account account = new Account();
				account.setEmail(this.email);
				account.setNickname(this.nickname);

				DataLine account_line = dc.line(this.email, Account.class);
				account = account_line.insert(account);

				tx.commit();

			} finally {
				IOTools.close(dc);
			}

		}

	}

}
