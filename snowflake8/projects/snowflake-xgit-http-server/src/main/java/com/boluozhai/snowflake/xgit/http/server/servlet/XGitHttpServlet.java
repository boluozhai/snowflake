package com.boluozhai.snowflake.xgit.http.server.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet;
import com.boluozhai.snowflake.xgit.http.server.controller.XGitHttpCtrl;

public class XGitHttpServlet extends RestServlet {

	private static final long serialVersionUID = 9211200119788148719L;

	@Override
	protected Map<String, RestController> create_handler_table() {

		this.setDefaultHandler(new XGitHttpCtrl());

		Map<String, RestController> tab = new HashMap<String, RestController>();
		return tab;
	}

}
