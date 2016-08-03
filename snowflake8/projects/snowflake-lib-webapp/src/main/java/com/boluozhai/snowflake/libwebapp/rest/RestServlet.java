package com.boluozhai.snowflake.libwebapp.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RestServlet extends HttpServlet {

	private static final long serialVersionUID = -6829149650943898084L;

	private Map<String, RestController> _handlers;

	interface attr_key {

		String rest_type = "REST.type";
		String rest_id = "REST.id";

		String path_prefix = "REST";

	}

	public RestServlet() {
		super();
	}

	protected abstract Map<String, RestController> create_handler_table();

	private final RequestDispatcher getRequestDispatcher(String name) {
		Map<String, RestController> map = this._handlers;
		if (map == null) {
			map = this.create_handler_table();
			map = Collections.synchronizedMap(map);
			this._handlers = map;
		}
		RequestDispatcher handler = map.get(name);
		if (handler == null) {
			throw new RuntimeException("no handler for REST typeName: " + name);
		}
		return handler;
	}

	private final RequestDispatcher getRequestDispatcher(HttpServletRequest req) {
		URI uri = URI.create(req.getRequestURL().toString());
		String key = uri.getPath();
		String[] array = key.split("/");
		String type, id;
		type = id = null;
		for (int i = 0; i < array.length; i++) {
			String s = array[i];
			if (s.equalsIgnoreCase(attr_key.path_prefix)) {
				int i1 = i + 1;
				int i2 = i + 2;
				type = array[i1];
				if (i2 < array.length) {
					id = array[i2];
				}
				break;
			}
		}
		req.setAttribute(attr_key.rest_type, type);
		req.setAttribute(attr_key.rest_id, id);
		return this.getRequestDispatcher(type);
	}

	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher disp = this.getRequestDispatcher(req);
		disp.forward(req, resp);
	}

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher disp = this.getRequestDispatcher(req);
		disp.forward(req, resp);
	}

	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher disp = this.getRequestDispatcher(req);
		disp.forward(req, resp);
	}

	@Override
	protected final void doDelete(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher disp = this.getRequestDispatcher(req);
		disp.forward(req, resp);
	}

}
