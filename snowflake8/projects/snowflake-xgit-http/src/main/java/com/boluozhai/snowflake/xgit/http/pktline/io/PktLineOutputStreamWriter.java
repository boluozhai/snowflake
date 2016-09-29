package com.boluozhai.snowflake.xgit.http.pktline.io;

import java.io.IOException;
import java.io.OutputStream;

import com.boluozhai.snowflake.xgit.http.pktline.PktLine;
import com.boluozhai.snowflake.xgit.http.pktline.PktPayload;

public class PktLineOutputStreamWriter implements PktLineWriter {

	private final OutputStream out;

	public PktLineOutputStreamWriter(OutputStream o) {
		this.out = o;
	}

	public void write(PktLine line) throws IOException {

		PktPayload pay = line.getPayload();
		if (pay == null) {
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(0);
			return;
		}

		final int len = pay.getLength();
		final int off = pay.getOffset();
		final byte[] dat = pay.getData();

		final int pkt_len = 4 + len;
		line.setLength(pkt_len);

		// write pkt_len
		for (int i = 0; i < 4; i++) {
			int n = (pkt_len >> ((3 - i) * 4)) & 0x0f;
			char ch = 0;
			if (0 <= n && n <= 9) {
				ch = (char) ('0' + n);
			} else {
				ch = (char) ('a' + (n - 10));
			}
			out.write(ch);
		}

		// write pkt_payload
		out.write(dat, off, len);

	}

	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

}
