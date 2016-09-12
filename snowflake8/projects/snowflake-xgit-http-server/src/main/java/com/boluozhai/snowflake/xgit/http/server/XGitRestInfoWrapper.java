package com.boluozhai.snowflake.xgit.http.server;

import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;

public class XGitRestInfoWrapper implements XGitHttpInfo {

	private final RestInfo inner;

	public XGitRestInfoWrapper(RestInfo info) {
		this.inner = info;
	}

	@Override
	public String getRepoName() {
		return inner.type;
	}

	@Override
	public String getPathInRepo() {
		String[] id = inner.id;
		if (id == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String s : id) {
			if (s == null) {
				continue;
			}
			if (s.length() == 0) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append('/');
			}
			sb.append(s);
		}
		return sb.toString();
	}

}
