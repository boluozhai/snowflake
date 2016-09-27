package com.boluozhai.snowflake.xgit.http.server.controller.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfo;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfoHolder;

public class XgitUploadObjects extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		GitHttpInfo info = GitHttpInfo.Agent.get(request);
		myHolder holder = new myHolder();
		holder.setInfo(info);

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO(holder);
		view.handle(request, response);

	}

	public static class myHolder extends GitHttpInfoHolder {
	}

}
