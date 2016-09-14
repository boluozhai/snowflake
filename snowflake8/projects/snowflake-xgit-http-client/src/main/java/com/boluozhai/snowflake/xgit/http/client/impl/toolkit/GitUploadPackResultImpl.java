package com.boluozhai.snowflake.xgit.http.client.impl.toolkit;

import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.httpclient.HttpEntity;
import com.boluozhai.snowflake.httpclient.HttpRequest;
import com.boluozhai.snowflake.httpclient.HttpResponse;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackResult;
import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;

final class GitUploadPackResultImpl implements GitUploadPackResult {

	private final Inner inner;

	public GitUploadPackResultImpl(GitHttpService service) {
		this.inner = new Inner(service);
	}

	@Override
	public void load() throws IOException {
		HttpResponse response = null;
		try {
			HttpRequest request = inner.make_request();
			response = inner.exec(request);
			inner.check_response(response);
			inner.parse_response(response);
		} finally {
			IOTools.close(response);
		}
	}

	private static class Inner {

		private final GitHttpService service;

		public Inner(GitHttpService service) {
			this.service = service;
		}

		public HttpRequest make_request() {
			// TODO Auto-generated method stub

			HttpEntity ent = null;

			HttpRequest request = new HttpRequest();
			request.setMethod("POST");
			request.setEntity(ent);

			return request;
		}

		public HttpResponse exec(HttpRequest request) throws IOException {
			return service.execute(request);
		}

		public void check_response(HttpResponse response) throws IOException {
			int code = response.getCode();
			if (code != 200) {
				String msg = response.getMessage();
				throw new IOException("HTTP " + code + " " + msg);
			}

			String want = "application/x-git-upload-pack-result";
			String real = response.getEntity().type();
			if (!want.equals(real)) {
				String msg = "want a '%s', but receive a '%s'.";
				msg = String.format(msg, want, real);
				throw new IOException(msg);
			}

		}

		public void parse_response(HttpResponse response) throws IOException {
			PktLineReader reader = null;
			try {
				HttpEntity ent = response.getEntity();
				String type = ent.type();
				System.out.println(type);

				InputStream in = ent.input();
				reader = new PktLineReader(in);
				for (;;) {
					PktLine line = reader.read();
					if (line == null) {
						break;
					} else {
						this.onLine(line);
					}
				}
			} finally {
				IOTools.close(reader);
			}
		}

		private void onLine(PktLine line) {
			// TODO Auto-generated method stub

			int len = line.getLength();
			System.out.format("[%d]\n", len);

		}

	}

}
