package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.xgit.http.server.GitHttpController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfo;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfoHolder;

public class InfoRefsCtrl extends GitHttpController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		GitHttpInfo info = GitHttpInfo.Agent.get(request);
		myHolder holder = new myHolder();
		holder.setInfo(info);
		myHolder.check_url(request, info);

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(holder);
		view.handle(request, response);

	}

	public static class myHolder extends GitHttpInfoHolder {

		public static void check_url(HttpServletRequest request,
				GitHttpInfo info) {

			if ("info/refs".equals(info.resource)) {
				// ok
			} else {
				String msg = "bad path : " + info.resource;
				throw new SnowflakeException(msg);
			}

		}
	}

}
