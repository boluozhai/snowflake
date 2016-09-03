package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.api.h2o.RepositoryModel;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;

public class RepositoryCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JsonRestView view = new JsonRestView();
		try {

			RepositoryModel pojo = new RepositoryModel();
			view.setResponsePOJO(pojo);

		} finally {
			view.forward(request, response);
		}

	}

}