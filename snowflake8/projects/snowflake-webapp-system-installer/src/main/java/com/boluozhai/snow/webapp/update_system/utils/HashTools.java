package com.boluozhai.snow.webapp.update_system.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.boluozhai.snowflake.util.IOTools;

public class HashTools {

	public static String sha256(File file) throws IOException {
		InputStream in = null;
		try {
			String alg = "sha-256";
			MessageDigest md = MessageDigest.getInstance(alg);
			in = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			for (;;) {
				final int cb = in.read(buffer);
				if (cb < 0) {
					break;
				} else {
					md.update(buffer, 0, cb);
				}
			}
			byte[] result = md.digest();
			return toString(result);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} finally {
			IOTools.close(in);
		}
	}

	private static String toString(byte[] ba) {
		StringBuilder sb = new StringBuilder();
		for (byte b : ba) {
			String s = Integer.toHexString(b & 0xff);
			if (s.length() == 1) {
				sb.append('0');
			}
			sb.append(s);
		}
		return sb.toString();
	}

}
