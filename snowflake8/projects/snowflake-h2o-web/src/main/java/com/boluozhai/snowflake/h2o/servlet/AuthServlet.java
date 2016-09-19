package com.boluozhai.snowflake.h2o.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = 5020086899440824920L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/javascript");

		String auth = req.getHeader("Authorization");
		if (auth == null) {

			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setHeader("WWW-Authenticate", "Basic");

		} else if (auth.length() > 10) {
			// OK

			resp.getOutputStream().println("//ok " + auth);

		} else {
			// jump

			// String script = "window.location=Login.html";
			// resp.getOutputStream().println(script);

			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setHeader("WWW-Authenticate", "Basic");

		}

	}

}
