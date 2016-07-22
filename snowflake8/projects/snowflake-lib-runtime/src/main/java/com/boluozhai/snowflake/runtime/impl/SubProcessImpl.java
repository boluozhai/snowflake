package com.boluozhai.snowflake.runtime.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.boluozhai.snow.util.IOTools;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.runtime.ErrorHandler;
import com.boluozhai.snowflake.runtime.LineHandler;
import com.boluozhai.snowflake.runtime.SubProcess;

public class SubProcessImpl implements SubProcess {

	private final InnerSPInfo _info;
	private Writer _writer;
	private Runnable _runnable;
	private Thread _thread;
	private boolean _do_cancel;
	private boolean _is_done;

	public SubProcessImpl(InnerSPInfo info) {
		this._info = info;
	}

	@Override
	public SnowContext getContext() {
		return _info.context;
	}

	@Override
	public String getCommand() {
		return _info.command;
	}

	@Override
	public String[] getCommandLines() {
		return InnerSPInfo.toStringArray(_info.command);
	}

	@Override
	public LineHandler getOutputHandler() {
		return _info.h_output;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return _info.h_error;
	}

	@Override
	public Writer getInputWriter() {
		return this._writer;
	}

	@Override
	public Runnable getRunnable() {
		Runnable r = this._runnable;
		if (r == null) {
			r = new MyMainRunnable();
			this._runnable = r;
		}
		return r;
	}

	@Override
	public void start() {
		Runnable runnable = this.getRunnable();
		Thread thread = new Thread(runnable);
		this._thread = thread;
		thread.start();
	}

	@Override
	public void join() throws InterruptedException {
		this._thread.join();
	}

	@Override
	public void cancel() {
		this._do_cancel = true;
	}

	@Override
	public void kill() {
		this._thread.interrupt();
	}

	@Override
	public boolean done() {
		return this._is_done;
	}

	private class MyMainRunnable implements Runnable {

		@Override
		public void run() {

			InputStream in = null;
			InputStream err = null;
			OutputStream out = null;

			String enc = "utf-8";
			Throwable error = null;
			final SubProcessImpl self = SubProcessImpl.this;

			try {

				Runtime rt = Runtime.getRuntime();
				String cmd = self._info.command;

				Process proc = rt.exec(cmd);
				in = proc.getInputStream();
				out = proc.getOutputStream();
				err = proc.getErrorStream();

				self._writer = new OutputStreamWriter(out, enc);

				final OutputStream h_err = new MyOutputAdapter(
						self._info.h_error);
				final OutputStream h_out = new MyOutputAdapter(
						self._info.h_output);

				MyPumpRunnable p1 = new MyPumpRunnable(in, h_out);
				MyPumpRunnable p2 = new MyPumpRunnable(err, h_err);

				Thread thread = new Thread(p1);
				thread.start();
				p2.run();
				thread.join();

			} catch (Exception e) {

				error = e;

			} finally {

				IOTools.close(in);
				IOTools.close(out);
				IOTools.close(err);

				if (error != null) {
					self._info.h_error.onError(self, error);
				}

				self._is_done = true;

			}

		}
	}

	private class MyPumpRunnable implements Runnable {

		private final OutputStream out;
		private final InputStream in;

		public MyPumpRunnable(InputStream i, OutputStream o) {
			this.in = i;
			this.out = o;
		}

		@Override
		public void run() {
			try {
				IOTools.pump(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class MyOutputAdapter extends OutputStream {

		private final LineHandler handler;
		private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		public MyOutputAdapter(LineHandler h) {
			this.handler = h;
		}

		@Override
		public void write(int b) throws IOException {
			if (b == 0x0a || b == 0x0d) {
				this.flush_line();
			} else {
				baos.write(b);
			}
		}

		private void flush_line() throws UnsupportedEncodingException {

			if (baos.size() <= 0) {
				return;
			}

			String s = baos.toString("utf-8");
			baos.reset();

			final SubProcess self = SubProcessImpl.this;
			this.handler.onLine(self, s);
		}
	}

}
