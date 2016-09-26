package com.boluozhai.snowflake.rest.server.info;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;
import com.boluozhai.snowflake.rest.server.support.AgentRestRequestInfoFactory;

public interface RestRequestInfo {

	public class Factory {

		public static RestRequestInfo getInstance(HttpServletRequest request) {
			RestRequestInfoFactory factory = new AgentRestRequestInfoFactory();
			return factory.getInstance(request);
		}

	}

	HttpServletRequest getRequest();

	SnowflakeContext getContext();

	SessionInfo getSessionInfo();

	PathInfo getPathInfo();

}
