package com.boluozhai.snowflake.installer.min.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.util.IOTools;

public class BinaryTools {

	public static void save(byte[] ba, File file, boolean mkdirs)
			throws IOException {

		OutputStream out = null;
		try {
			if (mkdirs) {
				File dir = file.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
			out = new FileOutputStream(file);
			out.write(ba);
		} finally {
			IOTools.close(out);
		}

	}

	public static void close(Closeable in) {

		if (in == null) {
			return;
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void pump(InputStream in, OutputStream out)
			throws IOException {

		byte[] buffer = new byte[1024 * 16];
		for (;;) {
			final int cb = in.read(buffer);
			if (cb < 0) {
				break;
			} else {
				out.write(buffer, 0, cb);
			}
		}

	}

}
