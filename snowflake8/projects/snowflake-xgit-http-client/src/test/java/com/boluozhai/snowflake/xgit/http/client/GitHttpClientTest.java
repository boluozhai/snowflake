package com.boluozhai.snowflake.xgit.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;

import org.junit.Test;

import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.util.IOTools;

public class GitHttpClientTest {

	@Test
	public void test() {

		String href = "https://github.com/git/git.git";

		Tester tester = null;
		Testing testing = null;
		InputStream in = null;
		HttpConnection conn = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			GitHttpClient client = GitHttpClient.Factory.getInstance(context);
			GitHttpRepo rc = client.connect(URI.create(href));
			GitHttpResource res = rc
					.getResource(GitHttpResource.PATH.info_refs);
			GitHttpService service = res
					.getService(GitHttpService.NAME.git_upload_pack);

			conn = service.open();
			in = conn.getInputStream();
			PrintStream out = System.out;
			IOTools.pump(in, out);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			tester.close(testing);
			IOTools.close(in);
			IOTools.close(conn);
		}

	}

}
