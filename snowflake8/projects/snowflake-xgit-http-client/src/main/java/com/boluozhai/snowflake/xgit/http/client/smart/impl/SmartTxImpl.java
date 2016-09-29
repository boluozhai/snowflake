package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartRx;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartTx;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;

final class SmartTxImpl implements SmartTx {

	private final String resource;
	private final String service;
	private Repository local_repo;
	private GitHttpRepo remote_repo;

	private ObjectBank bank;

	private InnerSmartConnection remote_conn;
	private InnerSmartConnection local_conn;

	public SmartTxImpl(SmartClient client, String resource, String service) {

		this.resource = resource;
		this.service = service;
		this.local_repo = client.getLocalRepository();
		this.remote_repo = client.getRemoteRepository();

		this.bank = ObjectBank.Factory.getBank(local_repo);
	}

	@Override
	public void close() throws IOException {

		InnerSmartConnection l = this.local_conn;
		InnerSmartConnection r = this.remote_conn;

		this.local_conn = null;
		this.remote_conn = null;
		this.bank = null;
		this.local_repo = null;
		this.remote_repo = null;

		IOTools.close(l);
		IOTools.close(r);

	}

	private InnerSmartConnection inner_get_remote_conn() throws IOException {
		InnerSmartConnection con = this.remote_conn;
		if (con == null) {
			GitHttpService ser = this.remote_repo.getService(resource, service);
			con = new InnerRemoteConn(ser, this.bank);
			this.remote_conn = con;
		}
		return con;
	}

	private InnerSmartConnection inner_get_local_conn() {
		InnerSmartConnection con = this.local_conn;
		if (con == null) {
			con = new InnerLocalConn();
			this.local_conn = con;
		}
		return con;
	}

	@Override
	public void write(SmartPkt pkt) throws IOException {

		InnerSmartConnection to = null;
		String cmd = pkt.getCommand();

		if (cmd == null) {
			// to remote
		} else if (cmd.equals(SmartPkt.COMMAND.want)) {
			ObjectId id = pkt.getId();
			GitObject obj = this.bank.object(id);
			if (obj.exists()) {
				to = this.inner_get_local_conn();
			}
		} else if (cmd.equals(SmartPkt.COMMAND.have)) {
			// to remote
		} else {
			// to remote
		}

		if (to == null) {
			to = this.inner_get_remote_conn();
		}

		to.tx(pkt);

	}

	@Override
	public SmartRx openRx() {
		return new SmartRxImpl(this.local_conn, this.local_conn);
	}

}
