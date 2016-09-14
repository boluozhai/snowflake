package com.boluozhai.snowflake.httpclient.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.httpclient.HttpEntity;

public class StringEntity implements HttpEntity {

	private String _enc;
	private String _type;
	private String _string;
	private byte[] _bytes;

	public StringEntity(String s, String type) {
		this._string = s;
		this._type = type;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setEncoding(String enc) {
		_enc = enc;
	}

	public void setString(String s) {
		_string = s;
	}

	@Override
	public long length() {
		try {
			byte[] ba = this.toByteArray();
			return ba.length;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String type() {
		return _type;
	}

	@Override
	public String encoding() {
		return this._enc;
	}

	@Override
	public InputStream input() throws IOException {
		byte[] ba = this.toByteArray();
		return new ByteArrayInputStream(ba);
	}

	@Override
	public byte[] toByteArray() throws IOException {
		byte[] ba = this._bytes;
		if (ba == null) {
			String enc = this._enc;
			if (enc == null) {
				enc = "utf-8";
			}
			ba = _string.getBytes(enc);
			this._bytes = ba;
		}
		return ba;
	}

	@Override
	public String toString(String enc) throws IOException {
		if (enc != null) {
			this._enc = enc;
		}
		return _string;
	}

}
