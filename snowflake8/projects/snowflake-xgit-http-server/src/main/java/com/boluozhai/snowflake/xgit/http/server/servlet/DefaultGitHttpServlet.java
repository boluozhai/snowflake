package com.boluozhai.snowflake.xgit.http.server.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.xgit.http.server.GitHttpController;
import com.boluozhai.snowflake.xgit.http.server.controller.InfoRefsCtrl;
import com.boluozhai.snowflake.xgit.http.server.controller.NullCtrl;
import com.boluozhai.snowflake.xgit.http.server.controller.RepoHomeCtrl;

public final class DefaultGitHttpServlet extends AbstractGitHttpServlet {

	private static final long serialVersionUID = -8391940221525255502L;

	@Override
	protected Map<String, GitHttpController> create_handler_table() {

		Map<String, GitHttpController> tab = new HashMap<String, GitHttpController>();

		tab.put(SERVICE.HOME, new RepoHomeCtrl());
		tab.put(SERVICE.FORBIDDEN, new NullCtrl());
		tab.put(SERVICE.INFO_REFS, new InfoRefsCtrl());

		tab.put("info/refs#git-receive-pack", new InfoRefsCtrl());
		tab.put("info/refs#git-upload-pack", new InfoRefsCtrl());

		return tab;

	}

}
