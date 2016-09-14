package com.boluozhai.snowflake.xgit.http.client.impl;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.httpclient.HttpClient;
import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.httpclient.HttpRequest;
import com.boluozhai.snowflake.httpclient.HttpResponse;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpResource;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;

final class GitHttpServiceImpl implements GitHttpService {

	private static class Inner {

		private final String name;
		private final GitHttpResource resource;
		private final HttpClient http_client;
		private final URI location;

		public Inner(GitHttpResource res, String name) {

			GitHttpClient client = res.getOwner().getClient();

			this.resource = res;
			this.name = name;
			this.http_client = client.getHttpClient();
			this.location = make_location(res, name);
		}

		private static URI make_location(GitHttpResource res, String name) {
			URI loc = res.getURI();
			String str = loc.toString();
			int index = str.indexOf('?');
			if (index >= 0) {
				str = str.substring(0, index);
			}
			return URI.create(str + "?service=" + name);
		}

	}

	private final Inner inner;

	public GitHttpServiceImpl(GitHttpResource res, String name) {
		this.inner = new Inner(res, name);
	}

	@Override
	public GitHttpResource getOwner() {
		return inner.resource;
	}

	@Override
	public URI getURI() {
		return inner.location;
	}

	@Override
	public String getServiceName() {
		return inner.name;
	}

	@Override
	public HttpConnection open() throws IOException {
		URI uri = inner.location;
		return inner.http_client.open(uri);
	}

	@Override
	public HttpResponse execute(HttpRequest request) throws IOException {

		if (request == null) {
			request = new HttpRequest();
		}

		URI url = this.getURI();
		request.setUrl(url);

		return inner.http_client.execute(request);
	}

}
