package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;
import java.util.LinkedList;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;

final class InnerLocalConn implements InnerSmartConnection {

	private LinkedList<SmartPkt> fifo;

	public InnerLocalConn() {
		this.fifo = new LinkedList<SmartPkt>();
	}

	@Override
	public void tx(SmartPkt pkt) {
		pkt = new SmartPkt(pkt);
		this.fifo.add(pkt);
	}

	@Override
	public SmartPkt rx() {
		LinkedList<SmartPkt> li = this.fifo;
		if (li == null) {
			return null;
		} else {
			return li.pollFirst();
		}
	}

	@Override
	public void close() throws IOException {
		this.fifo = null;
	}

}
