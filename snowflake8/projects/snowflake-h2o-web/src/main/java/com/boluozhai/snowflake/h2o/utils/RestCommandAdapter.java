package com.boluozhai.snowflake.h2o.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.vfs.VFile;

public class RestCommandAdapter {

	private VFile _path;
	private SnowflakeContext _context;
	private MyResponse _response;

	public RestCommandAdapter(SnowflakeContext context) {
		this._context = context;
	}

	public void setPath(VFile path) {
		this._path = path;
	}

	public SnowflakeContext createChildContext() throws IOException {

		MyResponse response = new MyResponse();
		response.init();
		this._response = response;

		SnowflakeContext parent = this._context;
		ContextBuilder builder = SnowContextUtils.getContextBuilder(parent,
				SnowContextUtils.FactoryName.child);

		builder.setURI(this._path.toURI());
		CLIResponse.Agent.setResponse(builder, response);

		return builder.create();
	}

	public String getResultText() throws UnsupportedEncodingException {
		MyResult result = this._response.makeResult();
		return result.getMessage();
	}

	private class MyResult {

		private ByteArrayOutputStream _out;
		private ByteArrayOutputStream _err;

		private String _message;
		private String _error_message;

		public MyResult() {
			this._out = new ByteArrayOutputStream(1024);
			this._err = new ByteArrayOutputStream(1024);
		}

		public String getMessage() {
			return this._message;
		}

		public MyResult create() throws UnsupportedEncodingException {

			final String enc = "utf-8";
			ByteArrayOutputStream baos;
			byte[] ba;

			baos = this._out;
			ba = baos.toByteArray();
			this._message = new String(ba, enc);

			baos = this._err;
			ba = baos.toByteArray();
			this._error_message = new String(ba, enc);

			return this;
		}

		public OutputStream err() {
			return new MyLimitOutputStream(this._err);
		}

		public OutputStream out() {
			return new MyLimitOutputStream(this._out);
		}
	}

	private class MyLimitOutputStream extends OutputStream {

		private final OutputStream out;

		public MyLimitOutputStream(OutputStream o2) {
			this.out = o2;
		}

		@Override
		public void write(int b) throws IOException {
			out.write(b);
		}

		public void write(byte[] b) throws IOException {
			out.write(b);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
		}

		public void flush() throws IOException {
			out.flush();
		}

		public void close() throws IOException {
			out.close();
		}

	}

	private class MyResponse implements CLIResponse {

		private PrintStream _err;
		private PrintStream _out;
		private MyResult _result;

		public MyResponse() {
		}

		public MyResult makeResult() throws UnsupportedEncodingException {
			this._err.flush();
			this._out.flush();
			return this._result.create();
		}

		public void init() throws IOException {

			MyResult result = new MyResult();

			OutputStream out = result.out();
			OutputStream err = result.err();

			boolean autoFlush = true;
			String encoding = "utf-8";

			this._out = new PrintStream(out, autoFlush, encoding);
			this._err = new PrintStream(err, autoFlush, encoding);
			this._result = result;

		}

		@Override
		public PrintStream out() {
			return this._out;
		}

		@Override
		public PrintStream err() {
			return this._err;
		}
	}

}
