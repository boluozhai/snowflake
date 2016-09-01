package com.boluozhai.snowflake.h2o.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.h2o.rest.controller.RestFileController;
import com.boluozhai.snowflake.h2o.rest.controller.RestRepoController;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet;

public class H2ORestServlet extends RestServlet {

	private static final long serialVersionUID = 5402870026807169966L;

	@Override
	protected Map<String, RestController> create_handler_table() {

		Map<String, RestController> table = new HashMap<String, RestController>();
		table.put("file", new RestFileController());
		table.put("repository", new RestRepoController());
		return table;

	}

}
