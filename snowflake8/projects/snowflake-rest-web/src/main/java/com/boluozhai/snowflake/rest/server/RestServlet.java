package com.boluozhai.snowflake.rest.server;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public abstract class RestServlet extends HttpServlet {

	private static final long serialVersionUID = -6829149650943898084L;

	private Map<String, RestController> _handlers;

	protected boolean _enable_log;

	interface attr_key {

		// String rest_type = "REST.type";
		// String rest_id = "REST.id";
		// String path_prefix = "REST";

		String rest_info = RestInfo.class.getName();

	}

	public static class RestInfo {

		public String baseURI;
		public String app;
		public String api;
		public String type;
		public String[] id;

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

	public static RestInfo getRestInfo(ServletRequest req) {
		return (RestInfo) req.getAttribute(attr_key.rest_info);
	}

	private final RequestDispatcher getRequestDispatcher(HttpServletRequest req) {

		// info

		RestInfo info = null;
		// info = RestServlet.getRestInfo(req);
		info = new RestInfo();

		// path

		URI uri = URI.create(req.getRequestURL().toString());
		String basepath = req.getContextPath();
		String fullpath = uri.getPath();

		List<String> id_list = new ArrayList<String>();
		String res = fullpath.substring(basepath.length());
		String[] array = res.split("/");
		int i = 0;
		for (String part : array) {
			part = part.trim();
			if (part.length() == 0) {
				continue;
			}
			switch (i++) {
			case 0:
				info.api = part;
				break;
			case 1:
				info.type = part;
				break;
			default:
				id_list.add(part);
				break;
			}
		}

		// save to info
		info.id = id_list.toArray(new String[id_list.size()]);
		info.app = basepath;

		req.setAttribute(attr_key.rest_info, info);

		if (this._enable_log) {
			Gson gs = new Gson();
			String msg = gs.toJson(info);
			System.out.println(msg);
		}

		return this.getRequestDispatcher(info.type);
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
