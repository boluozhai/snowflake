package com.boluozhai.snowflake.util;

public final class SystemAttributes {

	public static String get(String key) {
		return get(key, true);
	}

	public static String get(String key, boolean required) {
		String value = System.getProperty(key);
		if ((value == null) && (required)) {
			String msg = "need a property in java.lang.System: " + key;
			throw new RuntimeException(msg);
		}
		return value;
	}

}
