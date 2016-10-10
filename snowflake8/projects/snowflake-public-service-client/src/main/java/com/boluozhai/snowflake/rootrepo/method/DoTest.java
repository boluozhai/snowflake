package com.boluozhai.snowflake.rootrepo.method;

import java.io.IOException;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.httpclient.HttpRequest;
import com.boluozhai.snowflake.httpclient.HttpResponse;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;

public class DoTest {

	private final GitHttpService _service;

	public DoTest(SnowflakeContext context, GitHttpService service) {
		this._service = service;
	}

	public void play() throws IOException {

		HttpRequest request = new HttpRequest();
		HttpResponse response = this._service.execute(request);

		// TODO
		String s = response.getEntity().toString(null);
		System.out.println(s);

	}

}
