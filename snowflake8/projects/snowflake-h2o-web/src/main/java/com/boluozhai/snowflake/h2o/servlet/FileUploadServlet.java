package com.boluozhai.snowflake.h2o.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.server.RestRequestHandler;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 4771209528659979045L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		SnowflakeContext context = WebContextUtils.getWebContext(req);
		context.getBean("rest:gateway:root");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		SnowflakeContext context = WebContextUtils.getWebContext(req);
		RestRequestHandler handler = (RestRequestHandler) context
				.getBean("rest:gateway:plain-file");
		handler.handle(req, resp);

	}

}
