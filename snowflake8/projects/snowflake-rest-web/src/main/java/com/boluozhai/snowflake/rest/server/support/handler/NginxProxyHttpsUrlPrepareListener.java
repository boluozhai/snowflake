package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NginxProxyHttpsUrlPrepareListener implements RestRequestListener {

	private static final String key_base = NginxProxyHttpsUrlPrepareListener.class
			.getName();

	public static final String key_url = key_base + ".url_base";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final StringBuffer url = request.getRequestURL();
		final String scheme1 = request.getHeader("x-front-scheme");

		if (scheme1 != null) {
			final String str = url.toString();
			final URI uri = URI.create(str);
			final String scheme2 = uri.getScheme();
			if (!scheme1.equals(scheme2)) {
				final int i = str.indexOf(':');
				url.setLength(0);
				url.append(scheme1);
				url.append(str.substring(i));
			}
		}

		request.setAttribute(key_url, url.toString());

	}

	public static String getURL(HttpServletRequest request) {

		Object url = request.getAttribute(key_url);
		if (url instanceof String) {
			return url.toString();
		} else {
			return request.getRequestURL().toString();
		}

	}

}
