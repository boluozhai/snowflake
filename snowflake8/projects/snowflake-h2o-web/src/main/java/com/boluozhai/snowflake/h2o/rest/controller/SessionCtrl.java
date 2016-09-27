package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.h2o.rest.helper.H2oRestInfo;
import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.google.gson.Gson;

public class SessionCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		H2oRestInfo path_info = H2oRestInfo.getInstance(request);

		String[] ids = path_info.getId().toArray();

		if (ids[ids.length - 1].endsWith(".js")) {
			this.rest_response_js(request, response);
			return;

		} else {
		}

		JsonRestView view = new JsonRestView();
		try {

			SessionModel pojo = new SessionModel();
			view.setResponsePOJO(pojo);

		} finally {
			view.handle(request, response);
		}

	}

	private void rest_response_js(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		RestRequestInfo rest_info = RestRequestInfo.Factory
				.getInstance(request);

		SessionModel model = rest_info.getSessionInfo().getModel();

		Gson gs = new Gson();
		String json = gs.toJson(model);

		StringBuilder sb = new StringBuilder();
		sb.append("this.com.boluozhai.snowflake.web.SessionInfo.data = ");
		sb.append(json);
		sb.append(';');

		String enc = "utf-8";
		byte[] ba = sb.toString().getBytes(enc);

		// response.setContentLength(ba.length);
		response.setContentType("text/javascript");
		request.getRequestDispatcher("/js/controller/session.js").include(
				request, response);
		response.getOutputStream().write(ba);

	}

}
