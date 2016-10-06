package com.boluozhai.snowflake.installer.min.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.boluozhai.snowflake.installer.min.utils.BinaryTools;

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

		InputStream in = null;
		HttpURLConnection con = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			URL addr = new URL(url);
			con = (HttpURLConnection) addr.openConnection();
			int code = con.getResponseCode();
			if (code != 200) {
				String msg = con.getResponseMessage();
				String txt = "http %d %s";
				txt = String.format(txt, code, msg);
				throw new IOException(txt);
			}

			in = con.getInputStream();
			BinaryTools.pump(in, baos);
			return baos.toByteArray();

		} finally {
			BinaryTools.close(in);
			con.disconnect();
		}

	}

}
