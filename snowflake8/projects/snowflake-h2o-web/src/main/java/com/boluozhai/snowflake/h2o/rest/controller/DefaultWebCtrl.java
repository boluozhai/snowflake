package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;

public class DefaultWebCtrl extends RestController {

	interface DEFINE {

		String xxxx = "x";

	}

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MyPathMaker maker = new MyPathMaker(request);

		if (maker.need_redirect()) {
			maker.do_redirect(response);
			return;
		}

		String path = maker.create();
		// System.out.println("disp2: " + path);
		request.getRequestDispatcher(path).forward(request, response);

	}

	private static class MyPathMaker {

		private String[] _array;
		private String _redirect_2;
		private String _file_name;

		public MyPathMaker(HttpServletRequest request) {

			RestRequestInfo rest_info = RestRequestInfo.Factory
					.getInstance(request);
			PathInfo path_info = rest_info.getPathInfo();

			String user = path_info.getPartString("user");
			String repo = path_info.getPartString("repository");
			String api = path_info.getPartString("api");
			String type = path_info.getPartString("type");

			if (user != null) {
				user = DEFINE.xxxx;
			}

			if (repo != null) {
				repo = DEFINE.xxxx;
			}

			String[] a1 = { user, repo, api, type };
			this._array = a1;

			String url = request.getRequestURI();
			this._redirect_2 = url;

			final int ifn = url.lastIndexOf('/');
			if (ifn < 0) {
				this._file_name = url;
			} else {
				this._file_name = url.substring(ifn);
			}

		}

		public void do_redirect(HttpServletResponse response) {
			response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
			response.setHeader("Location", this._redirect_2 + "/");
		}

		public boolean need_redirect() {

			String fn = this._file_name;
			if (fn == null) {
			} else if (fn.equals("/")) {
			} else if (fn.endsWith(".png")) {
			} else if (fn.endsWith(".jpg")) {
			} else if (fn.endsWith(".gif")) {
			} else if (fn.endsWith(".js")) {
			} else if (fn.endsWith(".css")) {
			} else if (fn.endsWith(".html")) {

			} else if (fn.endsWith(".git")) {
			} else if (fn.endsWith(".sf")) {
			} else {
				return true;
			}

			return false;

		}

		public String create() {

			String[] data = this._array;
			String path = "/www/user-repo/%s-%s-%s-%s%s";

			path = String.format(path, data[0], data[1], data[2], data[3],
					this._file_name);

			return path;

		}
	}

}
