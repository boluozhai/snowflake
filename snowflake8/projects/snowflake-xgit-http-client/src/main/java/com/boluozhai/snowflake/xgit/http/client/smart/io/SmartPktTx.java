package com.boluozhai.snowflake.xgit.http.client.smart.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.PktPayload;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineWriter;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class SmartPktTx implements SmartPktWriter {

	// private final Repository repo;
	private final PktLineWriter out;
	private final SmartPktHandler handler;
	private final PktLineBuilder builder;
	private final ObjectBank bank;

	public SmartPktTx(PktLineWriter out, Repository repo, SmartPktHandler h) {

		if (h == null) {
			h = new DefaultSmartPktHandler();
		}

		// this.repo = repo;
		this.out = out;
		this.handler = h;
		this.builder = new PktLineBuilder();
		this.bank = ObjectBank.Factory.getBank(repo);
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

	@Override
	public void write(SmartPkt pkt) throws IOException {

		final String cmd = pkt.getCommand();

		if (cmd == null) {
			// skip

		} else if (cmd.equals(SmartPkt.COMMAND.entity)) {
			this.inner_send_entity_pkt(pkt);

		} else if (cmd.equals(SmartPkt.COMMAND.want)) {
			this.inner_send_simple_pkt(pkt);

		} else if (cmd.equals(SmartPkt.COMMAND.have)) {
			this.inner_send_simple_pkt(pkt);

		} else {
			this.inner_send_simple_pkt(pkt);
		}

	}

	private void inner_send_entity_pkt(SmartPkt pkt) throws IOException {

		InputStream in = null;

		try {

			final SmartPktWrapper pkt_wrapper = new SmartPktWrapper(pkt);
			final ObjectId id = pkt.getId();
			final GitObject obj = bank.object(id);
			final GitObjectEntity ent = obj.entity();
			final long zip_size = obj.zippedSize();
			final byte[] buffer = new byte[65500 - 2000];

			in = ent.openZippedInput();
			long offset = 0;

			for (;;) {
				final int cb = in.read(buffer);
				if (cb < 0) {
					break;
				}
				final long remain = zip_size - (offset + cb);
				pkt_wrapper.setPosition(offset, cb, remain);
				this.inner_send_simple_pkt(pkt, buffer, 0, cb);
				offset += cb;
			}

		} finally {
			IOTools.close(in);
		}

	}

	private void inner_send_simple_pkt(SmartPkt pkt) throws IOException {
		this.inner_send_simple_pkt(pkt, null, 0, 0);
	}

	private void inner_send_simple_pkt(SmartPkt pkt, byte[] data, int off,
			int len) throws IOException {
		builder.reset();
		builder.put_head(pkt);
		if ((data != null) && (len > 0)) {
			builder.append_entity(data, off, len);
		}
		PktLine line = builder.create();
		this.out.write(line);
		this.handler.onTx(pkt);
	}

	private static class SmartPktWrapper {

		private final SmartPkt pkt;

		public SmartPktWrapper(SmartPkt pkt) {
			this.pkt = pkt;
		}

		public void setPosition(long offset, int cb, long remain) {
			pkt.setOffset(offset);
			pkt.setLength(cb);
			pkt.setRemain(remain);
		}

	}

	private static class PktLineBuilder {

		private final ByteArrayOutputStream baos;
		private SmartPkt head;

		private PktLineBuilder() {
			this.baos = new ByteArrayOutputStream();
		}

		public void put_head(SmartPkt pkt) {
			this.head = pkt;
		}

		public void append_entity(byte[] data, int off, int len)
				throws IOException {
			this.flush_head();
			this.baos.write(data, off, len);
		}

		private void flush_head() throws IOException {

			if (baos.size() > 0) {
				return;
			}

			final SmartPkt pkt = this.head;
			final StringBuilder sb = new StringBuilder();

			final String cmd = pkt.getCommand();
			final ObjectId id = pkt.getId();
			final ObjectId id2 = pkt.getId2();
			final String ref = pkt.getRef();

			if (cmd != null) {
				sb.append(cmd).append(' ');
			}
			if (id != null) {
				sb.append(id).append(' ');
			}
			if (id2 != null) {
				sb.append(id2).append(' ');
			}
			if (ref != null) {
				sb.append(ref).append(' ');
			}

			Map<String, String> param = new HashMap<String, String>();
			param.put("accept", pkt.getAccept());
			param.put("type", pkt.getType());
			param.put("length", Long.toString(pkt.getLength()));
			param.put("offset", Long.toString(pkt.getOffset()));
			param.put("remain", Long.toString(pkt.getRemain()));
			param.put("plain-size", Long.toString(pkt.getPlainSize()));

			param.put("entity", String.valueOf(pkt.isContainEntity()));
			param.put("recursion", String.valueOf(pkt.isRecursion()));

			List<String> keys = new ArrayList<String>(param.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				String value = param.get(key);
				sb.append(';').append(key);
				sb.append('=').append(value);
			}

			final String enc = "utf-8";
			byte[] ba = sb.toString().getBytes(enc);
			baos.write(ba);

		}

		public PktLine create() throws IOException {
			this.flush_head();
			byte[] ba = baos.toByteArray();
			PktPayload pl = new PktPayload();
			pl.set(ba);
			PktLine line = new PktLine();
			line.setPayload(pl);
			return line;
		}

		public void reset() {
			this.baos.reset();
		}

	}

}
