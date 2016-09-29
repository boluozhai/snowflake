package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.PktPayload;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineOutputStreamWriter;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;

final class InnerRemoteConn implements InnerSmartConnection {

	private final HttpConnection conn;
	private final ObjectBank bank;
	private OutputStream out;
	private PktLineOutputStreamWriter pkt_wtr;

	public InnerRemoteConn(GitHttpService ser, ObjectBank bank)
			throws IOException {

		this.conn = ser.open();
		this.bank = bank;

		this.open();

	}

	private void open() throws IOException {

		OutputStream out = this.conn.getOutputStream();
		PktLineOutputStreamWriter writer = new PktLineOutputStreamWriter(out);

		this.out = out;
		this.pkt_wtr = writer;
	}

	@Override
	public void tx(SmartPkt pkt) throws IOException {

		String cmd = pkt.getCommand();
		if (cmd == null) {
			if (pkt.isContainEntity()) {
				this.inner_tx_object_entity(pkt);
				return;
			} else {
			}
		} else if (cmd.equals(SmartPkt.COMMAND.have)) {
		} else if (cmd.equals(SmartPkt.COMMAND.want)) {
		} else {
		}

		this.inner_tx_object_meta(pkt);
	}

	private static class InnerPktLineBuilder {

		private final SmartPkt pkt;
		private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		public InnerPktLineBuilder(SmartPkt pkt) {
			this.pkt = new SmartPkt(pkt);
		}

		public void reset() {
			baos.reset();
		}

		public void appendEntity(byte[] data, int off, int len)
				throws IOException {
			this.flush_head();
			baos.write(data, off, len);
		}

		private void flush_head() throws IOException {

			if (baos.size() > 0) {
				return;
			}


		}

		public PktLine create() throws IOException {
			this.flush_head();
			PktPayload pl = new PktPayload();
			pl.set(baos.toByteArray());
			PktLine line = new PktLine();
			line.setPayload(pl);
			return line;
		}

		public void setPosition(long offset, int cb, long remain) {
			pkt.setOffset(offset);
			pkt.setLength(cb);
			pkt.setRemain(remain);
		}

	}

	private void inner_tx_object_meta(SmartPkt pkt) throws IOException {

		InnerPktLineBuilder builder = new InnerPktLineBuilder(pkt);

		final PktLineOutputStreamWriter wtr = this.pkt_wtr;
		wtr.write(builder.create());

	}

	private void inner_tx_object_entity(SmartPkt pkt) throws IOException {


	}

	@Override
	public SmartPkt rx() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		IOTools.close(out);
		conn.close();
	}

}
