package com.boluozhai.snowflake.xgit.http.pktline.io;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.pktline.PktLine;

public interface PktLineReader extends Closeable {

	PktLine read() throws IOException;

}
