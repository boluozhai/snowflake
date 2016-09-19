package com.boluozhai.snowflake.rest.server;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.google.gson.Gson;

public class JsonRestPojoLoader {

	interface Key {

		String pojo = "request.JSON.POJO";

	}

	private final ServletRequest _request;
	private final Gson _gson;

	public JsonRestPojoLoader(ServletRequest request) {
		this._request = request;
		this._gson = new Gson();
	}

	private String load_data() throws IOException {
		ServletInputStream in = null;
		try {
			in = this._request.getInputStream();
			return TextTools.load(in);
		} finally {
			IOTools.close(in);
		}
	}

	private String get_data() throws IOException {
		final String key = Key.pojo;
		String data = (String) this._request.getAttribute(key);
		if (data == null) {
			data = this.load_data();
			this._request.setAttribute(key, data);
		}
		return data;
	}

	public <T> T getPOJO(Class<T> type) throws IOException {
		String str = this.get_data();
		return this._gson.fromJson(str, type);
	}

}
