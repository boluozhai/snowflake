package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultRequestHandler extends RestRequestFilter {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RestRequestListener[] list = this.getListeners();
		if (list != null) {
			for (RestRequestListener li : list) {
				li.handle(request, response);
			}
		}

		this.getNextHanlder().handle(request, response);

	}

}
