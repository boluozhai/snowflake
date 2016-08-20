package com.boluozhai.snowflake.webapp.system.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.manager.WebAppManager;
import com.boluozhai.snowflake.libwebapp.rest.JsonRestView;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;

public class RestExampleServlet extends HttpServlet {

	private static final long serialVersionUID = 8855603702639524520L;

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JsonRestView view = new JsonRestView();

		try {

			ServletContext sc = request.getServletContext();
			SnowflakeContext context = WebContextUtils.getWebContext(sc);

			WebAppManager webapp_man = WebAppManager.Factory
					.getManager(context);

			Object abc = "abc";
			view.setResponsePOJO(abc);

		} finally {
			view.forward(request, response);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

}
