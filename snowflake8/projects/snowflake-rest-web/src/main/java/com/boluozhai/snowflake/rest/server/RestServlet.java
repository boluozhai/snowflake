package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;

public class RestServlet extends HttpServlet implements RequestHandler {

	private static final long serialVersionUID = -6829149650943898084L;
	private RequestHandler _next_handler;

	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.handle(req, resp);
	}

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.handle(req, resp);
	}

	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.handle(req, resp);
	}

	@Override
	protected final void doDelete(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		this.handle(req, resp);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			RequestHandler next = this.get_next_handler();
			next.handle(request, response);

		} catch (Exception e) {

			e.printStackTrace();
			JsonRestExceptionView view = new JsonRestExceptionView();
			view.setException(e);
			view.handle(request, response);

		} finally {
			// NOP
		}

	}

	private RequestHandler get_next_handler() {
		RequestHandler next = this._next_handler;
		if (next == null) {
			next = this.load_next_handler();
			this._next_handler = next;
		}
		return next;
	}

	private RequestHandler load_next_handler() {
		String key = "handler";
		String bean_id = this.getInitParameter(key);
		if (bean_id == null) {
			String msg = "the handler id not set: " + key;
			throw new SnowflakeException(msg);
		}
		ServletContext sc = this.getServletContext();
		SnowflakeContext context = WebContextUtils.getWebContext(sc);
		return context.getBean(bean_id, RequestHandler.class);
	}

}
