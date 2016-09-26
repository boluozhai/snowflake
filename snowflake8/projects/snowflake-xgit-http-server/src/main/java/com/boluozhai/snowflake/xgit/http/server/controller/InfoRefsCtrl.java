package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.xgit.http.server.GitHttpController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfo;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfoHolder;

public class InfoRefsCtrl extends GitHttpController {

	@Override
	protected void git_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		GitHttpInfo info = this.getInfo(request);
		myHolder holder = new myHolder();
		holder.setInfo(info);

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(holder);
		view.handle(request, response);

	}

	public static class myHolder extends GitHttpInfoHolder {
	}

}
