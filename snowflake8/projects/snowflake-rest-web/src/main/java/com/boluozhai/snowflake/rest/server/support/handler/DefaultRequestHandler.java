package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.RestRequestHandler;

public class DefaultRequestHandler extends AbstractRequestHandler {

	private RestRequestHandler _cached_next;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestRequestListener[] list = this.getListeners();
		if (list != null) {
			for (RestRequestListener li : list) {
				li.handle(request, response);
			}
		}

		RestRequestHandler next = this.inner_get_next_head();
		next.handle(request, response);

	}

	private RestRequestHandler inner_get_next_head() {
		RestRequestHandler the_next = this._cached_next;
		if (the_next != null) {
			return the_next;
		}
		the_next = this.getNextHanlder();
		RestRequestFilter[] ftr_list = this.getFilters();
		if (ftr_list != null) {
			for (int i = ftr_list.length - 1; i >= 0; i--) {
				RestRequestFilter ftr = ftr_list[i];
				the_next = new MyFilterWrapper(ftr, the_next);
			}
		}
		this._cached_next = the_next;
		return the_next;
	}

	private static class MyFilterWrapper implements RestRequestHandler {

		private final RestRequestFilter filter;
		private final RestRequestHandler next;

		public MyFilterWrapper(RestRequestFilter f, RestRequestHandler n) {
			this.filter = f;
			this.next = n;
		}

		@Override
		public void handle(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {

			this.filter.doFilter(request, response, this.next);

		}

	}

}
