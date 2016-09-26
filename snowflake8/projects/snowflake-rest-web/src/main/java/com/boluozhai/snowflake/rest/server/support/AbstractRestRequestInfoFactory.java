package com.boluozhai.snowflake.rest.server.support;

import com.boluozhai.snowflake.rest.server.info.RestRequestInfoFactory;
import com.boluozhai.snowflake.rest.server.info.path.PathInfoFactory;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfoFactory;

public abstract class AbstractRestRequestInfoFactory implements
		RestRequestInfoFactory {

	private SessionInfoFactory sessionInfoFactory;
	private PathInfoFactory pathInfoFactory;

	public SessionInfoFactory getSessionInfoFactory() {
		return sessionInfoFactory;
	}

	public void setSessionInfoFactory(SessionInfoFactory sessionInfoFactory) {
		this.sessionInfoFactory = sessionInfoFactory;
	}

	public PathInfoFactory getPathInfoFactory() {
		return pathInfoFactory;
	}

	public void setPathInfoFactory(PathInfoFactory pathInfoFactory) {
		this.pathInfoFactory = pathInfoFactory;
	}

}
