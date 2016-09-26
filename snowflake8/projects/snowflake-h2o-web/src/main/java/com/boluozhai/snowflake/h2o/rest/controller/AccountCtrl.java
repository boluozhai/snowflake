package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.api.h2o.AccountModel;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;

public class AccountCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JsonRestView view = new JsonRestView();
		try {

			RestRequestInfo rri = RestRequestInfo.Factory.getInstance(request);
			rri.getContext();
			rri.getRequest();
			rri.getPathInfo();
			rri.getSessionInfo();

			AccountModel pojo = new AccountModel();
			view.setResponsePOJO(pojo);

		} finally {
			view.handle(request, response);
		}

	}

}
