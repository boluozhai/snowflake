package com.boluozhai.snowflake.h2o.rest.controller.res;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.api.h2o.SessionModel;
import com.boluozhai.snowflake.rest.element.session.SessionParam;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;

public class I18nJsCtrl extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RestRequestInfo info = RestRequestInfo.Factory.getInstance(request);
		SessionParam session = info.getSessionInfo().getModel().getSession();
		String lang = session.getLanguage();

		if (lang == null) {
			lang = "default";
		}

		String path = "/js/i18n/" + lang + ".js";

		response.setStatus(200);
		response.setContentType("application/javascript");

		ServletOutputStream out = response.getOutputStream();
		out.println("// " + path);

		try {
			request.getRequestDispatcher(path).include(request, response);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
		}

	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String lang = request.getParameter("language");
		if (lang == null) {
			throw new ServletException("need param: language");
		}

		RestRequestInfo rest_info = RestRequestInfo.Factory
				.getInstance(request);
		SessionInfo session_info = rest_info.getSessionInfo();
		SessionModel model = session_info.getModel();

		model.getSession().setLanguage(lang);

		session_info.setModel(model);

	}

}
