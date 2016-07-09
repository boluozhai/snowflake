package com.boluozhai.snow.discovery.utils;

import java.io.UnsupportedEncodingException;

import com.boluozhai.snow.discovery.scheme.PrivateScheme;
import com.boluozhai.snow.discovery.scheme.PublicScheme;

public class DiscoveryUtils {

	final static String enc = "utf-8";

	public static PublicScheme bytes2publicScheme(byte[] data, int offset,
			int length) throws UnsupportedEncodingException {
		String str = new String(data, enc);
		return PublicScheme.parse(str);
	}

	public static byte[] publicScheme2bytes(PublicScheme obj)
			throws UnsupportedEncodingException {
		String str = PublicScheme.toString(obj);
		return str.getBytes(enc);
	}

	public static PrivateScheme bytes2privateScheme(byte[] data, int offset,
			int length) throws UnsupportedEncodingException {
		String str = new String(data, offset, length, enc);
		return PrivateScheme.parse(str);
	}

	public static byte[] privateScheme2bytes(PrivateScheme obj)
			throws UnsupportedEncodingException {
		String str = PrivateScheme.toString(obj);
		return str.getBytes(enc);
	}

}
