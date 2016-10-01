package com.boluozhai.snowflake.xgit.http.server.controller.service;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.support.handler.NginxProxyHttpsUrlPrepareListener;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfoHolder;
import com.boluozhai.snowflake.xgit.http.server.controller.utils.ServiceHelper;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class XgitUploadRefs extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		StringBuffer url = request.getRequestURL();
		Enumeration<String> names = request.getHeaderNames();

		Repository repo = ServiceHelper.forRepository(request);
		URI location = repo.getComponentContext().getURI();

		String nginx = NginxProxyHttpsUrlPrepareListener.getURL(request);

		ServletOutputStream out = response.getOutputStream();
		out.println(this + ".with repo::" + location);

		out.println("request-url-to-nginx = " + nginx);
		out.println("request-url = " + url);
		out.println("request-headers");

		for (; names.hasMoreElements();) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			out.println("    " + name + " : " + value);
		}

	}

	public static class myHolder extends GitHttpInfoHolder {
	}

}
