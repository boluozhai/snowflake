package com.boluozhai.snowflake.xgit.http.pktline.io;

import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.PktPayload;

public class PktLineInputStreamReader implements PktLineReader {

	private final Inner inner;

	public PktLineInputStreamReader(InputStream in) {
		this.inner = new Inner(in);
	}

	@Override
	public void close() throws IOException {
		inner.in.close();
	}

	public PktLine read() throws IOException {
		return inner.read_a_line();
	}

	private static class Inner {

		private final InputStream in;
		private final byte[] buffer;
		private final byte[] zero_byte_array;

		public Inner(InputStream in) {
			this.in = in;
			this.buffer = new byte[4];
			this.zero_byte_array = new byte[0];
		}

		public PktLine read_a_line() throws IOException {
			int pkt_len = this.read_pkt_len();
			if (pkt_len < 0) {
				return null;
			}
			PktPayload payload = this.read_pkt_payload(pkt_len);
			if (payload == null) {
				return null;
			}
			PktLine line = new PktLine();
			line.setLength(pkt_len);
			line.setPayload(payload);
			return line;
		}

		private PktPayload read_pkt_payload(int pkt_len) throws IOException {

			int size = pkt_len - 4;
			if (size <= 0) {
				return this.make_a_empty_payload();
			}
			if (size > 65520) {
				throw new IOException("bad pkt-len: " + pkt_len);
			}

			byte[] buf = new byte[size];
			int cb = this.safe_read(buf);
			if (cb == size) {
				PktPayload pl = new PktPayload();
				pl.setData(buf);
				pl.setLength(buf.length);
				pl.setOffset(0);
				return pl;
			} else {
				throw new IOException("EOF in bad place.");
			}

		}

		private int safe_read(byte[] buf) throws IOException {
			return this.safe_read(buf, 0, buf.length);
		}

		private int safe_read(byte[] buf, int off, int len) throws IOException {
			int count = 0;
			for (; len > 0;) {
				int cb = in.read(buf, off, len);
				if (cb < 0) {
					break;
				} else {
					count += cb;
					len -= cb;
					off += cb;
				}
			}
			if ((len + count) == 0) {
				return 0;
			} else if (count > 0) {
				return count;
			} else {
				return -1;
			}
		}

		private PktPayload make_a_empty_payload() {
			PktPayload pl = new PktPayload();
			pl.setData(zero_byte_array);
			return pl;
		}

		private int read_pkt_len() throws IOException {
			int cb = this.safe_read(buffer, 0, 4);
			if (cb < 0) {
				return -1;
			}
			if (cb != 4) {
				throw new IOException("EOF in bad place.");
			}
			int len = 0;
			for (int i = 3; i >= 0; i--) {
				char ch = (char) (buffer[i]);
				int n = 0;
				if ('0' <= ch && ch <= '9') {
					n = ch - '0';
				} else if ('a' <= ch && ch <= 'f') {
					n = ch - 'a' + 10;
				} else {
					throw new IOException("bad pkt_len char: " + ch);
				}
				len |= (n << ((3 - i) * 4));
			}
			return len;
		}

	}

}
