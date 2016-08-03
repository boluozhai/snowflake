package com.boluozhai.snow.webapp.update_system.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.boluozhai.snowflake.util.IOTools;

public class HttpLoader {

	private final boolean _https_only;

	public HttpLoader(boolean https_only) {
		this._https_only = https_only;
	}

	public String loadString(String url) throws IOException {
		byte[] ba = loadBytes(url);
		return new String(ba, "utf-8");
	}

	public byte[] loadBytes(String url) throws IOException {

		if (this._https_only) {
			if (!url.startsWith("https://")) {
				throw new IOException("https only");
			}
		}

		HttpGet htGet = new HttpGet(url);

		HttpClientFactory factory = new HttpClientFactory();
		HttpClient client = factory.getClient();
		HttpResponse resp = client.execute(htGet);

		StatusLine status = resp.getStatusLine();
		int code = status.getStatusCode();
		if (code != 200) {
			String msg = status.getReasonPhrase();
			throw new IOException("HTTP " + code + " " + msg);
		}

		HttpEntity ent = resp.getEntity();
		InputStream in = ent.getContent();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] ba = null;

		try {
			IOTools.pump(in, baos);
			ba = baos.toByteArray();
		} finally {
			IOTools.close(in);
			IOTools.close(baos);
		}

		return ba;
	}

}
