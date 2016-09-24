package com.boluozhai.snowflake.h2o.command.sf.user;

import java.io.PrintStream;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.pojo.element.AliasItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.Account;
import com.boluozhai.snowflake.h2o.data.pojo.model.Alias;
import com.boluozhai.snowflake.h2o.data.pojo.model.Auth;
import com.boluozhai.snowflake.util.IOTools;

public class CmdAdd extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		Inner inner = new Inner(context);

		inner.load_param();
		inner.print_info();
		inner.check_param();
		inner.exec();

	}

	private static class Inner {

		private final SnowflakeContext context;
		private final PrintStream out;

		private String email;
		private String nickname;
		private String alias;

		public Inner(SnowflakeContext context) {

			CLIResponse resp = CLIResponse.Agent.getResponse(context);

			this.out = resp.out();
			this.context = context;

		}

		public void check_param() {

			// check email address format
			String addr = this.email;
			final int i1 = addr.indexOf('@');
			final int i2 = addr.indexOf('@', i1 + 1);
			if ((0 < i1) && (i1 < (addr.length() - 1)) && (i2 < 0)) {
				// goot
			} else {
				throw new SnowflakeException("bad email-address: " + addr);
			}

			// check alias
			String al = this.alias;
			if (al != null) {
				int i3 = al.indexOf('@');
				if (i3 < 0) {
					// good
				} else {
					throw new SnowflakeException("bad alias-name: " + al);
				}
			}

		}

		public void print_info() {
			out.println("create user");
			out.println("     email = " + email);
			out.println("     alias = " + alias);
			out.println("  nickname = " + nickname);
			out.println();
		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--email", "value");
			builder.option("--nickname", "value");
			builder.option("--alias", "value");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			this.email = ps.getRequiredOption("--email");
			this.nickname = ps.getOption("--nickname", "undefine");
			this.alias = ps.getOption("--alias", null);

		}

		public void exec() {

			DataClient client = null;

			try {
				client = H2oDataTable.openClient(context);

				Transaction tx = client.beginTransaction();

				final String id1 = this.email;
				final String id2 = this.alias;

				Account account = new Account();
				account.setEmail(this.email);
				account.setNickname(this.nickname);
				account = client.insert(id1, account);

				Alias alias1 = new Alias();
				alias1.setTo(null);
				alias1 = client.insert(id1, alias1);

				Auth auth = new Auth();
				auth = client.insert(id1, auth);

				if (id2 != null) {

					AliasItem to = new AliasItem();
					AliasItem from = new AliasItem();

					Alias alias2 = new Alias();
					to.setName(id1);
					alias2.setTo(to);
					alias2 = client.insert(id2, alias2);

					from.setName(id2);
					alias1.getFrom().put(id2, from);

				}

				tx.commit();

			} finally {
				IOTools.close(client);
			}

		}

	}

}
