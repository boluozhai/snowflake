package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.access.security.web.auth.WebAuthManager;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;

public class AuthCtrl extends RestController {

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletContext sc = request.getServletContext();
		SnowflakeContext context = WebContextUtils.getWebContext(sc);

		RestInfo info = this.getRestInfo(request);
		String auth_method = info.id[0];

		WebAuthManager am = WebAuthManager.Agent.getInstance(context);
		RestController handler = am.getHandler(auth_method);
		handler.forward(request, response);

	}

}
