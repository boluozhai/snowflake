package com.boluozhai.snowflake.xgit.http.client.smart.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.httpclient.HttpConnection;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.io.DefaultSmartPktRx;
import com.boluozhai.snowflake.xgit.http.client.smart.io.DefaultSmartPktTx;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktHandler;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktReader;
import com.boluozhai.snowflake.xgit.http.client.smart.io.SmartPktWriter;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineInputStreamReader;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineOutputStreamWriter;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineReader;
import com.boluozhai.snowflake.xgit.http.pktline.io.PktLineWriter;
import com.boluozhai.snowflake.xgit.repository.Repository;

final class InnerSmartCore {

	private final GitHttpService _service;
	private final Repository _repo;

	private SmartPktWriter _smart_tx;
	private SmartPktReader _smart_rx;
	private PktLineWriter _line_wtr;
	private PktLineReader _line_rdr;
	private OutputStream _output_stream;
	private InputStream _input_stream;
	private HttpConnection _http_conn;

	public InnerSmartCore(SmartClient client, String resource, String service) {
		GitHttpRepo remote = client.getRemoteRepository();
		this._service = remote.getService(resource, service);
		this._repo = client.getLocalRepository();
	}

	public SmartPktWriter open_tx() throws IOException {
		SmartPktWriter tx = this._smart_tx;
		if (tx == null) {
			PktLineWriter out = this.get_line_writer();
			Repository repo = this._repo;
			SmartPktHandler h = this.get_smart_pkt_handler();
			tx = new DefaultSmartPktTx(out, repo, h);
			this._smart_tx = tx;
		}
		return tx;
	}

	public SmartPktReader open_rx() throws IOException {
		SmartPktReader rx = this._smart_rx;
		if (rx == null) {
			PktLineReader in = this.get_line_reader();
			Repository repo = this._repo;
			SmartPktHandler h = this.get_smart_pkt_handler();
			rx = new DefaultSmartPktRx(in, repo, h);
			this._smart_rx = rx;
		}
		return rx;
	}

	private PktLineWriter get_line_writer() throws IOException {
		PktLineWriter wtr = this._line_wtr;
		if (wtr == null) {
			OutputStream out = this.get_output_stream();
			wtr = new PktLineOutputStreamWriter(out);
			this._line_wtr = wtr;
		}
		return wtr;
	}

	private PktLineReader get_line_reader() throws IOException {
		PktLineReader rdr = this._line_rdr;
		if (rdr == null) {
			InputStream in = this.get_input_stream();
			rdr = new PktLineInputStreamReader(in);
			this._line_rdr = rdr;
		}
		return rdr;
	}

	private HttpConnection get_http_connection() throws IOException {
		HttpConnection htc = this._http_conn;
		if (htc == null) {
			htc = this._service.open();
			this._http_conn = htc;
		}
		return htc;
	}

	private OutputStream get_output_stream() throws IOException {
		OutputStream out = this._output_stream;
		if (out == null) {
			HttpConnection con = this.get_http_connection();
			out = con.getOutputStream();
			this._output_stream = out;
		}
		return out;
	}

	private InputStream get_input_stream() throws IOException {
		InputStream in = this._input_stream;
		if (in == null) {
			HttpConnection con = this.get_http_connection();
			in = con.getInputStream();
			this._input_stream = in;
		}
		return in;
	}

	private SmartPktHandler get_smart_pkt_handler() {
		// TODO Auto-generated method stub
		return null;
	}

}
