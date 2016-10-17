package com.boluozhai.snowflake.h2o.rest.listener;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.rest.server.support.handler.RestRequestListener;

public class QueryParamEncodingAdjustListener implements RestRequestListener {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String[]> map = request.getParameterMap();
		for (String key : map.keySet()) {
			String[] item = map.get(key);
			for (int i = item.length - 1; i >= 0; i--) {
				String raw = item[i];
				byte[] ba = raw.getBytes("ISO-8859-1");
				item[i] = new String(ba, "UTF-8");
			}
		}

	}

}
