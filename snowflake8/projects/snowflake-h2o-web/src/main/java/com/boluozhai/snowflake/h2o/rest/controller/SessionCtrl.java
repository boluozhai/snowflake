package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.RestServlet.RestInfo;
import com.boluozhai.snowflake.rest.server.helper.SessionInfoHolder;
import com.google.gson.Gson;

public class SessionCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestInfo rest_info = this.getRestInfo(request);
		String[] ids = rest_info.id;

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
			view.forward(request, response);
		}

	}

	private void rest_response_js(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		SessionInfoHolder holder = SessionInfoHolder.create(request);
		SessionModel model = holder.get();

		Gson gs = new Gson();
		String json = gs.toJson(model);

		StringBuilder sb = new StringBuilder();
		sb.append("this.com.boluozhai.snowflake.web.SessionInfo = ");
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
