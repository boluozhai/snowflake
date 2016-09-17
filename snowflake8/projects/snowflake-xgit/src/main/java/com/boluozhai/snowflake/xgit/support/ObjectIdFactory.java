package com.boluozhai.snowflake.xgit.support;

import java.io.ByteArrayOutputStream;

import com.boluozhai.snowflake.xgit.HashId;
import com.boluozhai.snowflake.xgit.ObjectId;

public class ObjectIdFactory {

	public static ObjectId create(byte[] b) {
		String s = Tools.toString(b);
		return new ObjIdImpl(s, b);
	}

	public static ObjectId create(String s) {
		byte[] ba = Tools.parse(s);
		return new ObjIdImpl(s, ba);
	}

	private static class Tools {

		private final static char[] chs;

		static {
			String s = "0123456789abcdef";
			chs = s.toCharArray();
		}

		public static String toString(byte[] ba) {
			StringBuilder sb = new StringBuilder();
			for (byte b : ba) {
				int h = (b & 0xf0);
				int l = (b & 0x0f);
				sb.append(chs[h >> 4]);
				sb.append(chs[l]);
			}
			return sb.toString();
		}

		public static byte[] parse(String s) {
			final int len = s.length();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
			for (int i = 0; i < len; i += 2) {
				String s2 = s.substring(i, i + 2);
				int n = Integer.parseInt(s2, 16);
				baos.write(n);
			}
			return baos.toByteArray();
		}
	}

	private static class ObjIdImpl implements ObjectId {

		private final byte[] ba;
		private final String string;

		public ObjIdImpl(String s, byte[] b) {
			this.string = s;
			this.ba = b;

			if (s.length() < 20 || b.length < 10) {
				throw new RuntimeException("bad hash-id:" + s);
			}

		}

		public String toString() {
			String str = this.string;
			return str;
		}

		@Override
		public int hashCode() {
			return this.string.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof HashId) {
				String s1 = this.string;
				String s2 = obj.toString();
				return s1.equalsIgnoreCase(s2);
			} else {
				return false;
			}
		}

		@Override
		public byte[] toByteArray() {
			byte[] buffer = new byte[ba.length];
			for (int i = buffer.length - 1; i >= 0; i--) {
				buffer[i] = ba[i];
			}
			return buffer;
		}

	}

}
