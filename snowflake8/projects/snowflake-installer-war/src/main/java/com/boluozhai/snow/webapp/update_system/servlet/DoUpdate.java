package com.boluozhai.snow.webapp.update_system.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.boluozhai.snow.webapp.update_system.UpdateAgent;
import com.boluozhai.snow.webapp.update_system.UpdateClient;
import com.boluozhai.snow.webapp.update_system.UpdateContext;
import com.boluozhai.snow.webapp.update_system.UpdateContextBuilder;
import com.boluozhai.snow.webapp.update_system.pojo.WebResponse;
import com.google.gson.Gson;

public class DoUpdate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public DoUpdate() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		UpdateAgent tools = this.getUpdateTools(request);
		tools.check();

	}

	private UpdateAgent getUpdateTools(HttpServletRequest request) {
		ServletContext sc = request.getServletContext();
		WebApplicationContext ac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sc);

		UpdateContextBuilder builder = new UpdateContextBuilder(ac);
		UpdateContext context = builder.create();
		UpdateClient client = new UpdateClient(context);
		client.load_meta();
		return new UpdateAgent(client);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// check | download | install | auto

		UpdateAgent tools = this.getUpdateTools(request);
		String op = request.getParameter("op");
		WebResponse res = null;

		try {

			if (op == null) {
				res = this.make_error("bad op code: " + op);

			} else if (op.equalsIgnoreCase("check")) {
				res = tools.check();

			} else if (op.equalsIgnoreCase("download")) {
				res = tools.download();

			} else if (op.equalsIgnoreCase("install")) {
				res = tools.install();

			} else if (op.equalsIgnoreCase("auto")) {
				res = tools.auto();

			} else {
				res = this.make_error("bad op code: " + op);
			}

		} catch (Exception e) {

			e.printStackTrace();
			res = new WebResponse();
			res.setError(e.toString());

		} finally {
		}

		String enc = "utf-8";
		Gson gs = new Gson();
		String txt = gs.toJson(res);
		response.setContentType("application/json");
		response.getOutputStream().write(txt.getBytes(enc));

	}

	private WebResponse make_error(String text) {
		WebResponse res = new WebResponse();
		res.setError(text);
		return res;
	}
}
