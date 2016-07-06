package com.boluozhai.snow.tmp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class TmpClass {

	public static void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Writer stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Reader stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void pump(InputStream in, OutputStream out) throws IOException {
		pump(in, out, null);
	}

	public void pump(InputStream in, OutputStream out, byte[] buffer)
			throws IOException {
		if (buffer == null) {
			buffer = new byte[1024];
		}
		for (;;) {
			int cb = in.read(buffer);
			if (cb < 0) {
				break;
			} else {
				out.write(buffer, 0, cb);
			}
		}
	}

	public void pump(Reader in, Writer out) throws IOException {
		pump(in, out, null);
	}

	public void pump(Reader in, Writer out, char[] buffer) throws IOException {
		if (buffer == null) {
			buffer = new char[1024];
		}
		for (;;) {
			int cb = in.read(buffer);
			if (cb < 0) {
				break;
			} else {
				out.write(buffer, 0, cb);
			}
		}
	}

}
