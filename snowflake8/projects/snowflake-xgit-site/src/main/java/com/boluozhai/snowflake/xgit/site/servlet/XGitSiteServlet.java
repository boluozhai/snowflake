package com.boluozhai.snowflake.xgit.site.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet;
import com.boluozhai.snowflake.xgit.site.rest.controller.RestAccountCtrl;
import com.boluozhai.snowflake.xgit.site.rest.controller.RestAuthCtrl;
import com.boluozhai.snowflake.xgit.site.rest.controller.RestProfileCtrl;
import com.boluozhai.snowflake.xgit.site.rest.controller.RestRepositoryCtrl;

public class XGitSiteServlet extends RestServlet {

	private static final long serialVersionUID = 1967796591847278978L;

	public XGitSiteServlet() {
		this._enable_log = true;
	}

	@Override
	protected Map<String, RestController> create_handler_table() {
		Map<String, RestController> map = new HashMap<String, RestController>();

		map.put("Account", new RestAccountCtrl());
		map.put("Auth", new RestAuthCtrl());
		map.put("Repository", new RestRepositoryCtrl());
		map.put("Profile", new RestProfileCtrl());

		return map;
	}

}
