package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;

public class RestServlet extends HttpServlet implements RestRequestHandler {

	private static final long serialVersionUID = -6829149650943898084L;
	private RestRequestHandler _next_handler;

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

			RestRequestHandler next = this.get_next_handler(request);
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

	private RestRequestHandler get_next_handler(HttpServletRequest request) {
		RestRequestHandler next = this._next_handler;
		if (next == null) {
			next = this.load_next_handler(request);
			this._next_handler = next;
		}
		return next;
	}

	private RestRequestHandler load_next_handler(HttpServletRequest request) {
		String key = "handler";
		String bean_id = this.getInitParameter(key);
		if (bean_id == null) {
			String msg = "the handler id not set: " + key;
			throw new SnowflakeException(msg);
		}

		SnowflakeContext context = WebContextUtils.getWebContext(request);
		return context.getBean(bean_id, RestRequestHandler.class);
	}

}
