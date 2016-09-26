package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfoFactory;

public class InfoFactoryBindingListener extends RestRequestListener {

	private final static String key = "com.boluozhai.snowflake.rest.server.support.AgentRestRequestInfoFactory.factory";

	private RestRequestInfoFactory factory;

	public RestRequestInfoFactory getFactory() {
		return factory;
	}

	public void setFactory(RestRequestInfoFactory factory) {
		this.factory = factory;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestRequestInfoFactory f = this.factory;

		if (f == null) {
			String msg = "the property[%s] of the bean[%s] is not setted.";
			msg = String.format(msg, "factory", this);
			throw new SnowflakeException(msg);
		}

		request.setAttribute(key, f);

	}

}
