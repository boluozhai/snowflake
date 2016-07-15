package com.boluozhai.snowflake.xgit.objects;

import java.io.InputStream;
import java.io.OutputStream;

public interface GitObjectEntity {

	InputStream openPlainInput();

	InputStream openZippedInput();

	// OutputStream openPlainOutput();

	OutputStream openZippedOutput();

}
