package com.boluozhai.snowflake.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.boluozhai.snowflake.core.SnowflakeException;

public class HashTools {

	public static interface ALGORITHM {

		String md5 = "md5";

		String sha1 = "sha-1";
		String sha256 = "sha-256";
		String sha512 = "sha-512";

	}

	private final MessageDigest digest;

	public HashTools() {
		String algorithm = ALGORITHM.sha1;
		this.digest = Inner.create_md(algorithm);
	}

	public HashTools(String algorithm) {
		this.digest = Inner.create_md(algorithm);
	}

	public String getAlgorithm() {
		return this.digest.getAlgorithm();
	}

	private static class Inner {

		public static MessageDigest create_md(String algorithm) {
			try {
				return MessageDigest.getInstance(algorithm);
			} catch (NoSuchAlgorithmException e) {
				throw new SnowflakeException(e);
			}
		}

		private static final char[] hex_char_set = "0123456789abcdef"
				.toCharArray();

		public static String toString(byte[] ba) {
			char[] chs = hex_char_set;
			StringBuilder sb = new StringBuilder();
			for (byte b : ba) {
				int h = (b & 0xf0) >> 4;
				int l = (b & 0x0f);
				sb.append(chs[h]);
				sb.append(chs[l]);
			}
			return sb.toString();
		}

		private static byte[] toBytes(String s) {
			try {
				return s.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new SnowflakeException(e);
			}
		}

	}

	public byte[] hash(byte[] b) {
		digest.reset();
		return digest.digest(b);
	}

	public String hashString(byte[] b) {
		b = hash(b);
		return Inner.toString(b);
	}

	public String hashString(InputStream in, byte[] buffer) throws IOException {
		byte[] b = hash(in, null);
		return Inner.toString(b);
	}

	public byte[] hash(InputStream in, byte[] buffer) throws IOException {
		if (buffer == null) {
			buffer = new byte[1024 * 16];
		}
		digest.reset();
		for (;;) {
			int cb = in.read(buffer);
			if (cb < 0) {
				break;
			} else {
				digest.update(buffer, 0, cb);
			}
		}
		return digest.digest();
	}

	public byte[] hash(String s) {
		byte[] b = Inner.toBytes(s);
		return hash(b);
	}

	public String hashString(String s) {
		byte[] b = Inner.toBytes(s);
		return hashString(b);
	}

	public static String sha1string(InputStream in, byte[] buffer)
			throws IOException {
		HashTools ht = new HashTools(ALGORITHM.sha1);
		return ht.hashString(in, buffer);
	}

	public static String sha1string(String s) {
		HashTools ht = new HashTools(ALGORITHM.sha1);
		return ht.hashString(s);
	}

	public static String sha1string(byte[] ba) {
		HashTools ht = new HashTools(ALGORITHM.sha1);
		return ht.hashString(ba);
	}

}
