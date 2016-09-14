package com.boluozhai.snowflake.httpclient.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.httpclient.HttpClient;
import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.httpclient.HttpEntity;
import com.boluozhai.snowflake.httpclient.HttpRequest;
import com.boluozhai.snowflake.httpclient.HttpResponse;
import com.boluozhai.snowflake.util.IOTools;

public class HttpClientImpl implements HttpClient {

	private HttpClientImpl(SnowflakeContext context) {
	}

	public static HttpClient create(SnowflakeContext context) {
		return new HttpClientImpl(context);
	}

	@Override
	public HttpConnection open(URI uri) throws IOException {
		try {
			URL url = uri.toURL();
			HttpURLConnection htc = (HttpURLConnection) url.openConnection();
			return new HttpConnectionWrapper(htc);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public HttpResponse execute(HttpRequest request) throws IOException {
		InnerExe exe = new InnerExe();
		exe.request(request);
		exe.exec();
		return exe.response();
	}

	private class InnerExe {

		private HttpConnection _conn;
		private HttpResponse _response;
		private HttpRequest _request;

		public void request(HttpRequest request) throws IOException {

			final HttpClientImpl self = HttpClientImpl.this;
			final URI uri = request.getUrl();
			final HttpConnection conn = self.open(uri);
			this._conn = conn;
			this._request = request;

			// method
			String method = request.getMethod();
			if (method != null) {
				conn.setRequestMethod(method);
			}

			// headers
			Map<String, String> hds = request.getHeaders();
			if (hds != null) {
				for (String key : hds.keySet()) {
					String val = hds.get(key);
					conn.setRequestProperty(key, val);
				}
			}

			// entity
			HttpEntity entity = request.getEntity();
			if (entity == null) {
				return;
			}

			this.loadRequestEntityInfo(conn, entity);
			conn.setDoOutput(true);

			OutputStream out = null;
			InputStream in = null;
			try {
				out = conn.getOutputStream();
				in = entity.input();
				IOTools.pump(in, out);
			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}

		}

		private void loadRequestEntityInfo(HttpConnection conn,
				HttpEntity entity) {

			long len = entity.length();
			String type = entity.type();
			String enc = entity.encoding();

			if (len >= 0) {
				String key = "Content-Length";
				String val = String.valueOf(len);
				conn.setRequestProperty(key, val);
			}

			if (type != null) {
				String key = "Content-Type";
				String val = type;
				conn.setRequestProperty(key, val);
			}

			if (enc != null) {
				String key = "Content-Encoding";
				String val = enc;
				conn.setRequestProperty(key, val);
			}

		}

		public void exec() throws IOException {

			final HttpConnection conn = _conn;
			int code = conn.getResponseCode();
			String msg = conn.getResponseMessage();

			HttpResponse resp = new HttpResponse();
			resp.setCode(code);
			resp.setMessage(msg);
			this._response = resp;
		}

		public HttpResponse response() throws IOException {

			HttpResponse resp = _response;
			HttpConnection conn = _conn;

			// status
			resp.setRequest(_request);
			resp.setUrl(_request.getUrl());
			resp.setVersion(_request.getVersion());

			// headers
			Map<String, String> hds = new HashMap<String, String>();
			Map<String, List<String>> fields = conn.getHeaderFields();
			for (String key : fields.keySet()) {
				String val = conn.getHeaderField(key);
				hds.put(key, val);
			}
			resp.setHeaders(hds);

			// entity
			InputStream in = conn.getInputStream();
			HttpEntity entity = ResponseEntityImpl.create(in, resp);
			resp.setEntity(entity);

			return resp;
		}
	}

}
