package com.boluozhai.snowflake.xgit.vfs.impl.objectbank.pack;

import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.vfs.VFSContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;

public class BinaryReader {

	private static class Inner {

		private final InputStream in;

		public Inner(InputStream input) {
			this.in = input;
		}

		public int safe_read(byte[] buffer) throws IOException {
			return this.safe_read(buffer, 0, buffer.length);
		}

		public int safe_read(byte[] b, int off, int len) throws IOException {
			int count = 0;
			for (; len > 0;) {
				final int cb = in.read(b, off, len);
				if (cb < 0) {
					break;
				} else if (cb == 0) {
					this.safe_sleep(1);
				} else {
					count += cb;
					off += cb;
					len -= cb;
				}
			}
			return count;
		}

		private void safe_sleep(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private final Inner inner;

	public BinaryReader(VFile file) throws IOException {
		VFSContext context = file.vfs().context();
		VFSIO io = VFSIO.Agent.getInstance(context);
		InputStream in = io.input(file);
		this.inner = new Inner(in);
	}

	public String read_string(int cb) throws IOException {
		byte[] buffer = new byte[cb];
		int cb2 = this.inner.safe_read(buffer);
		String enc = "utf-8";
		return new String(buffer, 0, cb2, enc);
	}

	public int read_int(int cb) throws IOException {
		return (int) this.read_long(cb);
	}

	public long read_long(int cb) throws IOException {
		long n = 0;
		for (; cb > 0; cb--) {
			final int b = inner.in.read();
			if (b < 0) {
				break;
			} else {
				n = ((n << 8) | (b & 0xff));
			}
		}
		return n;
	}

	public int read_type_and_langth() throws IOException {
		// TODO Auto-generated method stub

		int[] j = new int[10];
		int[] k = new int[10];

		InputStream in = inner.in;
		for (int i = 0; i < j.length; i++) {
			final int b = in.read();
			if (b < 0) {
				break;
			}
			final int h = b & 0x80;
			final int l = b & 0x7f;
			j[i] = h;
			k[i] = l;
		}

		return -1;
	}

}
