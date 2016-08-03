package com.boluozhai.snowflake.webapp.example.servlet;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.utils.ContextPrinter;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;

public class ExampleServlet extends HttpServlet {

	private static final long serialVersionUID = 5263055060502847068L;

	public ExampleServlet() {
	}

	/*****
	 * the EXAMPLE for java webapp
	 * */

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// in builder style
		// String fn = SnowContextUtils.FactoryName.webapp;
		// ContextBuilder builder = SnowContextUtils.getContextBuilder(fn);
		// initial with builder ...
		// builder.create();

		// in simple style
		ServletContext sc = req.getServletContext();
		SnowContext context = WebContextUtils.getWebContext(sc);

		String enc = "utf-8";

		resp.setCharacterEncoding(enc);
		resp.setContentType("text/plain");
		ServletOutputStream out = resp.getOutputStream();
		PrintStream ps = new PrintStream(out, true, enc);

		ContextPrinter.print(context, ps);

	}

	/*****
	 * the EXAMPLE for java se application
	 * */

	public static void main(String[] arg) {

		// in builder style
		String fn = SnowContextUtils.FactoryName.javase;
		ContextBuilder builder = SnowContextUtils.getContextBuilder(fn);
		// initial with builder ...
		builder.create();

		// in simple style
		SnowContext context = SnowContextUtils.getAppContext(
				ExampleServlet.class, arg);
		context.getName();

	}

	/*****
	 * the EXAMPLE for junit test
	 * */

	public void test() {

		// in builder style
		String fn = SnowContextUtils.FactoryName.junit;
		ContextBuilder builder = SnowContextUtils.getContextBuilder(fn);
		// initial with builder ...
		builder.create();

		// in simple style
		SnowContext context = WebContextUtils.getJunitContext(this);
		context.getName();

	}
}
