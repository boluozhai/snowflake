package com.boluozhai.snowflake.xgit.http.pktline.io;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.pktline.PktLine;

public interface PktLineWriter extends Closeable {

	void write(PktLine line) throws IOException;

}
