package com.boluozhai.snowflake.installer.min.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.installer.min.InstallerApp;
import com.boluozhai.snowflake.installer.min.context.ApplicationContext;
import com.boluozhai.snowflake.installer.min.context.WebApplicationContextUtils;
import com.boluozhai.snowflake.installer.min.pojo.MetaFile;
import com.boluozhai.snowflake.installer.min.pojo.WebResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DoInstall extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public DoInstall() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		WebResponse pojo = new WebResponse();
		this.make_response(request, pojo);

		GsonBuilder gsb = new GsonBuilder();
		gsb.setPrettyPrinting();
		Gson gson = gsb.create();
		String str = gson.toJson(pojo);

		String enc = "utf-8";
		byte[] ba = str.getBytes(enc);

		response.setCharacterEncoding(enc);
		response.setContentLength(ba.length);
		response.setContentType("application/json");
		ServletOutputStream out = response.getOutputStream();
		out.write(ba);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this.do_install(request);
		this.doGet(request, response);

	}

	private void make_response(HttpServletRequest request, WebResponse res)
			throws IOException {

		ServletContext sc = request.getServletContext();
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sc);

		InstallerApp app = new InstallerApp(context);
		MetaFile meta = app.load();
		res.setMeta(meta);

	}

	private void do_install(HttpServletRequest request) throws IOException {

		ServletContext sc = request.getServletContext();
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sc);

		InstallerApp app = new InstallerApp(context);
		app.install();

	}

}
