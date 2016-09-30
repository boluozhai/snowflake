package com.boluozhai.snowflake.xgit.http.client.smart.io;

import java.io.IOException;
import java.io.OutputStream;

public class NopOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
	}

	@Override
	public void write(byte[] data) throws IOException {
	}

	@Override
	public void write(byte[] data, int off, int len) throws IOException {
	}

}
