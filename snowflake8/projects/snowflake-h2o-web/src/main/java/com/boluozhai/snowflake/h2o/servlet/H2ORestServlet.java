package com.boluozhai.snowflake.h2o.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.h2o.rest.controller.AccountCtrl;
import com.boluozhai.snowflake.h2o.rest.controller.AuthCtrl;
import com.boluozhai.snowflake.h2o.rest.controller.CommandCtrl;
import com.boluozhai.snowflake.h2o.rest.controller.FileCtrl;
import com.boluozhai.snowflake.h2o.rest.controller.RepoFileCtrl;
import com.boluozhai.snowflake.h2o.rest.controller.RepositoryCtrl;
import com.boluozhai.snowflake.h2o.rest.controller.SessionCtrl;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet;

public class H2ORestServlet extends RestServlet {

	private static final long serialVersionUID = 5402870026807169966L;

	@Override
	protected Map<String, RestController> create_handler_table() {

		Map<String, RestController> table = new HashMap<String, RestController>();

		table.put("account", new AccountCtrl());
		table.put("auth", new AuthCtrl());
		table.put("command", new CommandCtrl());
		table.put("file", new FileCtrl());
		table.put("working", new RepoFileCtrl());
		table.put("repository", new RepositoryCtrl());
		table.put("session", new SessionCtrl());

		return table;

	}

}
