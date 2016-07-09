package com.boluozhai.snow.discovery.scheme;

import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PrivateScheme extends PrivateSchemePojo {

	public final static String reg_prefix = "snowflake_discovery_private:";
	private final static byte[] reg_prefix_bytes = to_bytes(reg_prefix);

	private static byte[] to_bytes(String s) {
		try {
			return s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PrivateScheme parse(String s) {
		if (!s.startsWith(reg_prefix)) {
			return null;
		}
		s = s.substring(reg_prefix.length());
		GsonBuilder gsb = new GsonBuilder();
		Gson gs = gsb.create();
		return gs.fromJson(s, PrivateScheme.class);
	}

	public static String toString(PrivateScheme obj) {
		GsonBuilder gsb = new GsonBuilder();
		Gson gs = gsb.create();
		return reg_prefix + gs.toJson(obj);
	}

	public static boolean isPrivateScheme(byte[] buffer, int offset, int length) {
		if (buffer == null) {
			return false;
		}
		final byte[] reg = reg_prefix_bytes;
		if (length < reg.length) {
			return false;
		}
		for (int i = reg.length - 1; i >= 0; i--) {
			byte b1 = buffer[offset + i];
			byte b2 = reg[i];
			if (b1 != b2) {
				return false;
			}
		}
		return true;
	}

}
