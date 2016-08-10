package com.boluozhai.snowflake.webapp.system.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.libwebapp.rest.RestController;
import com.boluozhai.snowflake.libwebapp.rest.RestServlet;
import com.boluozhai.snowflake.webapp.system.rest.controller.ModuleCtrl;

public class RestRootServlet extends RestServlet {

	private static final long serialVersionUID = -7801384227858779965L;

	@Override
	protected Map<String, RestController> create_handler_table() {

		Map<String, RestController> table = new HashMap<String, RestController>();

		table.put("Module", new ModuleCtrl());

		return table;

	}

}
