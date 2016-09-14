package com.boluozhai.snowflake.httpclient.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.boluozhai.snowflake.httpclient.HttpEntity;
import com.boluozhai.snowflake.httpclient.HttpResponse;
import com.boluozhai.snowflake.util.IOTools;

final class ResponseEntityImpl implements HttpEntity {

	private long _length;
	private String _type;
	private String _encoding;
	private byte[] _byte_array;
	private InputStream _input;

	private ResponseEntityImpl(InputStream in, HttpResponse resp) {
		this._input = in;
	}

	public static HttpEntity create(InputStream in, HttpResponse resp) {
		ResponseEntityImpl entity = new ResponseEntityImpl(in, resp);
		InnerLoader.init(entity, resp);
		return entity;
	}

	@Override
	public long length() {
		return this._length;
	}

	@Override
	public String type() {
		return this._type;
	}

	@Override
	public String encoding() {
		return this._encoding;
	}

	@Override
	public InputStream input() {
		byte[] ba = this._byte_array;
		if (ba == null) {
			return this._input;
		} else {
			return new ByteArrayInputStream(ba);
		}
	}

	@Override
	public byte[] toByteArray() throws IOException {
		byte[] ba = this._byte_array;
		if (ba == null) {
			ba = InnerLoader.load_data(this);
			this._byte_array = ba;
		}
		return ba;
	}

	@Override
	public String toString(String enc) throws IOException {
		if (enc == null) {
			enc = this._encoding;
		}
		if (enc == null) {
			enc = "utf-8";
		}
		byte[] ba = this.toByteArray();
		return new String(ba, enc);
	}

	private static class InnerLoader {

		public static void init(ResponseEntityImpl self, HttpResponse resp) {

			Map<String, String> hds = resp.getHeaders();

			self._encoding = hds.get("Content-Encoding");
			self._type = hds.get("Content-Type");

			String length = hds.get("Content-Length");
			if (length != null) {
				self._length = Long.parseLong(length);
			}

		}

		public static byte[] load_data(ResponseEntityImpl self)
				throws IOException {
			InputStream in = null;
			OutputStream out = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				in = self._input;
				out = baos;
				IOTools.pump(in, out);
				return baos.toByteArray();
			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}
		}

	}

}
