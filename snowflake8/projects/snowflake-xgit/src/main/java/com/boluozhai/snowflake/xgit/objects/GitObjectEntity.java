package com.boluozhai.snowflake.xgit.objects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface GitObjectEntity {

	InputStream openPlainInput() throws IOException;

	InputStream openZippedInput() throws IOException;

	// OutputStream openPlainOutput() throws IOException;

	OutputStream openZippedOutput() throws IOException;

}
