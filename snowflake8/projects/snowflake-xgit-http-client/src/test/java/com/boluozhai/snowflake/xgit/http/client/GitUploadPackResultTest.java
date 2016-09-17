package com.boluozhai.snowflake.xgit.http.client;

import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitClientToolkit;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackRequest;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackResult;
import com.boluozhai.snowflake.xgit.http.client.toolkit.RemoteAgent;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;

public class GitUploadPackResultTest {

	// @Test
	public void test() {

		String href = "https://github.com/git/git.git";

		Tester tester = null;
		Testing testing = null;
		InputStream in = null;
		HttpConnection conn = null;
		PktLineReader reader = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			GitClientToolkit tk = GitClientToolkit.Factory.newInstance(context);
			RemoteAgent remote = tk.getRemote(href);
			GitUploadPackRequest request = remote.git_upload_pack();
			GitUploadPackResult res = request.requestResult();

			res.load();

		} catch (IOException e) {
			throw new RuntimeException(e);

		} finally {
			tester.close(testing);
			IOTools.close(in);
			IOTools.close(conn);
			IOTools.close(reader);
		}

	}

}
