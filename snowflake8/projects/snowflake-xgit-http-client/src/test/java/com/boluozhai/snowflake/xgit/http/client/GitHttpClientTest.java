package com.boluozhai.snowflake.xgit.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.junit.Test;

import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.PktPayload;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;

public class GitHttpClientTest {

	@Test
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

			GitHttpClient client = GitHttpClient.Factory.getInstance(context);
			GitHttpRepo rc = client.connect(URI.create(href));
			GitHttpResource res = rc
					.getResource(GitHttpResource.PATH.info_refs);
			GitHttpService service = res
					.getService(GitHttpService.NAME.git_upload_pack);

			conn = service.open();
			in = conn.getInputStream();
			reader = new PktLineReader(in);

			for (;;) {
				PktLine pl = reader.read();
				if (pl == null) {
					break;
				} else {
					this.log(pl);
				}
			}

			// PrintStream out = System.out;
			// IOTools.pump(in, out);

		} catch (IOException e) {

			throw new RuntimeException(e);

		} finally {
			tester.close(testing);
			IOTools.close(in);
			IOTools.close(conn);
			IOTools.close(reader);
		}

	}

	private void log(PktLine pl) throws UnsupportedEncodingException {

		String detail = "";
		int d = Math.abs(pl.getLength() - 60);
		if (d > 20) {

			PktPayload pay = pl.getPayload();
			byte[] dat = pay.getData();
			int len = pay.getLength();
			int off = pay.getOffset();
			int end = off + len;

			for (int i = off; i < end; i++) {
				int ch = dat[i];
				if (' ' <= ch && ch < 127) {
					// ok
				} else {
					dat[i] = '.';
				}
			}

			detail = new String(dat, off, len, "utf-8");

		}
		System.out.format("byte[%d]%s\n", pl.getLength(), detail);

	}

}
