package com.boluozhai.snowflake.xgit.http.client.smart.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPktWrapper;
import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.PktPayload;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class DefaultSmartPktRx implements SmartPktReader {

	interface define {

		String enc = "utf-8";
		String payload_offset = define.class.getName() + ".payload_offset";

		String opt_sep = String.valueOf(SmartPkt.DEFINE.opt_sep);
		String kv_sep = String.valueOf(SmartPkt.DEFINE.kv_sep);
		String param_sep = String.valueOf(SmartPkt.DEFINE.param_sep);

	}

	private final ObjectBank bank;
	private final PktLineReader in;
	private final SmartPktHandler handler;

	public DefaultSmartPktRx(PktLineReader in, Repository repo,
			SmartPktHandler h) {

		if (h == null) {
			h = new DefaultSmartPktHandler();
		}
		this.in = in;
		this.bank = ObjectBank.Factory.getBank(repo);
		this.handler = h;
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	private static class EntityBuilder implements Closeable {

		private final ObjectBank bank;
		private final ObjectId id;

		private OutputStream _output;
		private long _cnt_total;

		public EntityBuilder(ObjectId id, ObjectBank bank) {
			this.id = id;
			this.bank = bank;
		}

		private void open() throws IOException {

			GitObject obj = bank.object(id);
			if (obj.exists()) {
				// make a NOP output
				this._output = new NopOutputStream();
			} else {
				GitObjectEntity ent = obj.entity();
				this._output = ent.openZippedOutput();
			}

		}

		public void write(byte[] data, int offset, int length, long total_offset)
				throws IOException {

			if (this._cnt_total != total_offset) {
				String msg = "bad offset value, want:%d, get:%d.";
				msg = String.format(msg, this._cnt_total, total_offset);
				throw new SnowflakeException(msg);
			}

			OutputStream out = this._output;
			out.write(data, offset, length);

			this._cnt_total += length;

		}

		@Override
		public void close() throws IOException {
			this._output.close();
		}

	}

	private class PktGroup implements Closeable {

		private boolean _has_more = false;
		private SmartPkt _last_pkt;
		private EntityBuilder _entity_builder;

		public PktGroup() {
		}

		public boolean has_more() {
			return this._has_more;
		}

		public SmartPkt last_pkt() {
			return this._last_pkt;
		}

		public SmartPktWrapper parse_line(String head) throws IOException {

			final String[] array = head.split(define.opt_sep);
			final SmartPkt pkt = new SmartPkt();
			final Map<String, String> opt = pkt.getOption();
			final List<String> param = pkt.getParam();

			// parse command & param

			final String[] a2 = array[0].split(define.param_sep);
			final int max_command_length = 20;
			for (int i = 0; i < a2.length; i++) {
				final String s = a2[i].trim();
				if (s.length() < 1) {
					continue;
				}
				if (i == 0) {
					if (s.length() < max_command_length) {
						pkt.setCommand(s);
					} else {
						param.add(s);
					}
				} else {
					param.add(s);
				}
			}

			// parse option

			for (int j = 1; j < array.length; j++) {
				final String str = array[j];
				final int index = str.indexOf(SmartPkt.DEFINE.kv_sep);
				final String s1, s2;
				if (index < 0) {
					s2 = s1 = str.trim();
				} else {
					s1 = str.substring(0, index).trim();
					s2 = str.substring(index + 1).trim();
				}
				opt.put(s1, s2);
			}

			return new SmartPktWrapper(pkt);
		}

		public SmartPktWrapper parse_line(PktLine line) throws IOException {

			final PktPayload pl = line.getPayload();

			final byte[] data = pl.getData();
			final int offset = pl.getOffset();
			final int length = pl.getLength();
			final int end = offset + length;

			int i = pl.getOffset();
			for (; i < end; i++) {
				final int ch = data[i];
				if (ch == 0) {
					break;
				}
			}

			final String head = new String(data, offset, (i - offset),
					define.enc);
			final SmartPktWrapper pkt = this.parse_line(head);
			pkt.setOption(define.payload_offset, i + 1);
			return pkt;
		}

		public void handle_rx(SmartPktWrapper pkt, PktLine line)
				throws IOException {

			this._last_pkt = pkt.getPkt();

			String cmd = pkt.getCommand();
			String entity = pkt.getOption(SmartPkt.OPTION.containEntity);

			if (cmd == null && "true".equals(entity)) {
				this.handle_entity_pkt(pkt, line);
			} else {
				this.handle_simple_pkt(pkt, line);
			}

		}

		private void handle_simple_pkt(SmartPktWrapper pkt, PktLine line) {
			this._has_more = false;
		}

		private void handle_entity_pkt(SmartPktWrapper pkt, PktLine line)
				throws IOException {

			final ObjectId id = pkt.getIdParam(0);

			final long offset = pkt.getLongOption(SmartPkt.OPTION.offset);
			final long length = pkt.getLongOption(SmartPkt.OPTION.length);
			final long remain = pkt.getLongOption(SmartPkt.OPTION.remain);

			final PktPayload pl = line.getPayload();
			final byte[] win_data = pl.getData();
			final int win_length = (int) length;
			final int win_offset = pkt.getIntOption(define.payload_offset);

			EntityBuilder builder = this.getEntityBuilder(id, true);
			builder.write(win_data, win_offset, win_length, offset);

			this._has_more = (remain > 0);

		}

		private EntityBuilder getEntityBuilder(ObjectId id, boolean create)
				throws IOException {
			EntityBuilder eb = this._entity_builder;
			if (eb == null) {
				if (create) {
					eb = new EntityBuilder(id, bank);
					eb.open();
					this._entity_builder = eb;
				}
			} else {
				// check
				if (eb.id.equals(id)) {
					// ok
				} else {
					throw new SnowflakeException("bad pkt id:" + id);
				}
			}
			return eb;
		}

		@Override
		public void close() throws IOException {
			EntityBuilder builder = this.getEntityBuilder(null, false);
			if (builder == null) {
				return;
			} else {
				builder.close();
			}
		}

	}

	@Override
	public SmartPkt read() throws IOException {
		PktGroup group = null;
		try {
			group = new PktGroup();
			for (; group.has_more();) {
				PktLine line = in.read();
				if (line == null) {
					break;
				}
				SmartPktWrapper spw = group.parse_line(line);
				group.handle_rx(spw, line);
				this.handler.onRx(spw.getPkt());
			}
			return group.last_pkt();
		} finally {
			IOTools.close(group);
		}
	}

}
