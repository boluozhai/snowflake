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

		private String _redirect_2;
		private String _file_name;
		private String[] _path_elements;

		public MyPathMaker(HttpServletRequest request) {

			RestRequestInfo rest_info = RestRequestInfo.Factory
					.getInstance(request);
			PathInfo path_info = rest_info.getPathInfo();

			this._path_elements = path_info.getInAppPart().trimToArray();

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

			} else if (fn.endsWith(".js")) {
			} else if (fn.endsWith(".css")) {
			} else if (fn.endsWith(".html")) {

			} else if (fn.endsWith(".sf")) {
			} else if (fn.endsWith(".snow")) {
			} else if (fn.endsWith(".snowflake")) {
			} else if (fn.endsWith(".git")) {
			} else if (fn.endsWith(".xgit")) {

			} else if (fn.endsWith(".png")) {
			} else if (fn.endsWith(".jpg")) {
			} else if (fn.endsWith(".gif")) {

			} else {
				return true;
			}

			return false;

		}

		public String create() {

			String[] data = this._path_elements;
			this.setItem(data, 0, "user");
			this.setItem(data, 1, "repo");

			StringBuilder sb = new StringBuilder();
			for (String s : data) {
				sb.append('/').append(s);
			}

			if ("/".equals(this._file_name)) {
				sb.append('/');
			}

			return sb.toString();

		}

		private void setItem(String[] array, int index, String value) {
			if (index < array.length) {
				array[index] = value;
			}
		}
	}

}
