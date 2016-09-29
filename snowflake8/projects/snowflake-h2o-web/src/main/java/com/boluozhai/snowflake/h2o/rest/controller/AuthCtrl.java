package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.access.security.web.auth.WebAuthManager;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.h2o.rest.helper.H2oRestInfo;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.server.RestController;

public class AuthCtrl extends RestController {

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SnowflakeContext context = WebContextUtils.getWebContext(request);

		H2oRestInfo path_info = H2oRestInfo.getInstance(request);
		String auth_method = path_info.getId().toString();

		WebAuthManager am = WebAuthManager.Agent.getInstance(context);
		RestController handler = am.getHandler(auth_method);
		handler.handle(request, response);

	}

}
