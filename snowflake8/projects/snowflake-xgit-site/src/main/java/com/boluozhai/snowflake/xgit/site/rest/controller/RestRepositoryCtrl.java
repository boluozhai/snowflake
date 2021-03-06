package com.boluozhai.snowflake.xgit.site.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;

public class RestRepositoryCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JsonRestView view = new JsonRestView();

		try {

			view.setResponsePOJO("" + this);

		} finally {
			view.handle(request, response);
		}

	}

}
