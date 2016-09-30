package com.boluozhai.snowflake.xgit.http.server.controller.service;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPktWrapper;
import com.boluozhai.snowflake.xgit.http.client.smart.io.DefaultSmartPktRx;
import com.boluozhai.snowflake.xgit.http.client.smart.io.DefaultSmartPktTx;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktHandler;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktReader;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktWriter;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineInputStreamReader;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineOutputStreamWriter;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineWriter;
import com.boluozhai.snowflake.xgit.http.server.controller.utils.ServiceHelper;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class XgitUploadObjects extends RestController {

	@Override
	protected void rest_get(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Repository repo = ServiceHelper.forRepository(request);
		URI location = repo.getComponentContext().getURI();
		response.getOutputStream().println(this + ".with repo::" + location);

	}

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MySmartPktResponder responder = null;

		try {

			Repository repo = ServiceHelper.forRepository(request);
			responder = new MySmartPktResponder(repo);

			final String service = request.getParameter("service");
			final String reg_request_type = "application/x-" + service
					+ "-request";
			final String reg_result_type = "application/x-" + service
					+ "-result";
			final String request_type = request.getContentType();

			if (!reg_request_type.equals(request_type)) {
				String msg = "bad request context-type : " + request_type;
				throw new SnowflakeException(msg);
			}

			InputStream in = request.getInputStream();
			responder.readFrom(in);

			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(reg_result_type);
			OutputStream out = response.getOutputStream();
			responder.writeTo(out);

		} finally {

			IOTools.close(responder);

		}

	}

	private class MySmartPktHandler implements SmartPktHandler {

		@Override
		public void onTx(SmartPkt pkt) {
			this.log("server-tx", pkt);
		}

		@Override
		public void onRx(SmartPkt pkt) {
			this.log("server-rx", pkt);
		}

		private void log(String tag, SmartPkt pkt) {

			StringBuilder sb = new StringBuilder();
			sb.append(pkt.getCommand());
			List<String> pa = pkt.getParam();
			for (String s : pa) {
				sb.append(' ').append(s);
			}

			String cn = this.getClass().getSimpleName();
			String msg = "[%s:%h].%s: %s";
			int code = this.hashCode() | 0x80000000;
			msg = String.format(msg, cn, code, tag, sb);
			System.out.println(msg);
		}

		@Override
		public boolean accept(SmartPkt pkt) {
			return false;
		}

	}

	private class MySmartPktResponder implements Closeable {

		private final Repository repo;
		private final SmartPktHandler handler;
		private final List<Closeable> close_later;
		private final List<ObjectId> want_list;

		public MySmartPktResponder(Repository repo) {
			this.repo = repo;
			this.handler = new MySmartPktHandler();
			this.close_later = new ArrayList<Closeable>();
			this.want_list = new ArrayList<ObjectId>();
		}

		public void readFrom(InputStream in) throws IOException {

			PktLineReader line_reader = new PktLineInputStreamReader(in);
			SmartPktReader pkt_reader = new DefaultSmartPktRx(line_reader,
					repo, handler);

			this.close_later.add(pkt_reader);

			for (;;) {
				SmartPkt pkt = pkt_reader.read();
				if (pkt == null) {
					break;
				} else {
					SmartPktWrapper wp = new SmartPktWrapper(pkt);
					String cmd = wp.getCommand();
					if (cmd == null) {
						// NOP
					} else if (cmd.equals(SmartPkt.COMMAND.want)) {
						this.want_list.add(wp.getIdParam(0));
					} else {
						// NOP
					}
				}
			}

		}

		public void writeTo(OutputStream out) throws IOException {

			PktLineWriter line_writer = new PktLineOutputStreamWriter(out);
			SmartPktWriter pkt_writer = new DefaultSmartPktTx(line_writer,
					repo, handler);
			this.close_later.add(pkt_writer);
			final ObjectBank bank = ObjectBank.Factory.getBank(repo);

			for (ObjectId id : this.want_list) {
				GitObject obj = bank.object(id);
				if (obj.exists()) {
					SmartPkt pkt = new SmartPkt();
					pkt.setCommand(SmartPkt.COMMAND.entity);
					pkt.getParam().add(id.toString());
					pkt_writer.write(pkt);
				}
			}

		}

		@Override
		public void close() throws IOException {
			for (Closeable cl : this.close_later) {
				IOTools.close(cl);
			}
		}

	}

}
