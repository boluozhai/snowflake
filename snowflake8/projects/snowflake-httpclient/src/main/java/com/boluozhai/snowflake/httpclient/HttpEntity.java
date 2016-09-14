package com.boluozhai.snowflake.httpclient;

import java.io.IOException;
import java.io.InputStream;

public interface HttpEntity {

	long length();

	String type();

	String encoding();

	InputStream input();

	byte[] toByteArray() throws IOException;

	String toString(String enc) throws IOException;

}
