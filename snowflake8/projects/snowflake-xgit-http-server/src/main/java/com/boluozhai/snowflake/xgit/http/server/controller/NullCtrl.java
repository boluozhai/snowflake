package com.boluozhai.snowflake.xgit.http.server.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.xgit.http.server.GitHttpController;
import com.boluozhai.snowflake.xgit.http.server.GitHttpInfoHolder;

public class NullCtrl extends GitHttpController {

	@Override
	protected void git_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		JsonRestView view = new JsonRestView();
		view.setResponsePOJO("http 403 forbidden");
		view.forward(request, response);

	}

	public static class myHolder extends GitHttpInfoHolder {
	}

}
