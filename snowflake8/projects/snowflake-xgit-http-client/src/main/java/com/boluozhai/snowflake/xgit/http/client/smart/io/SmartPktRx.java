package com.boluozhai.snowflake.xgit.http.client.smart.io;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class SmartPktRx implements SmartPktReader {

	private final Repository repo;
	private final PktLineReader in;
	private final SmartPktHandler handler;

	private SmartPktRx(PktLineReader in, Repository repo, SmartPktHandler h) {

		if (h == null) {
			h = new DefaultSmartPktHandler();
		}

		this.in = in;
		this.repo = repo;
		this.handler = h;
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public SmartPkt read() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
