package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartRx;

final class SmartRxImpl implements SmartRx {

	private InnerSmartConnection con_l;
	private InnerSmartConnection con_r;
	private InnerSmartConnection current;

	public SmartRxImpl(InnerSmartConnection local, InnerSmartConnection remote) {
		this.con_l = local;
		this.con_r = remote;
	}

	@Override
	public void close() throws IOException {
		this.con_l = null;
		this.con_r = null;
	}

	@Override
	public SmartPkt read() throws IOException {
		for (int tout = 3; tout > 0; tout--) {
			InnerSmartConnection con = this.inner_get_current();
			if (con == null) {
				return null;
			}
			SmartPkt pkt = con.rx();
			if (pkt == null) {
				this.current = null;
			} else {
				return pkt;
			}
		}
		return null;
	}

	private InnerSmartConnection inner_get_current() {
		InnerSmartConnection con = this.current;
		if (con == null) {
			con = this.con_l;
			this.con_l = null;
			if (con == null) {
				con = this.con_r;
				this.con_r = null;
			}
			this.current = con;
		}
		return con;
	}

}
