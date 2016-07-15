package com.boluozhai.snowflake.xgit.objects;

import java.io.IOException;
import java.io.OutputStream;

public interface GitObjectBuilder {

	String type();

	long length();

	void write(byte[] buffer, int offset, int length) throws IOException;

	OutputStream getOutputStream();

	GitObject create();

}
