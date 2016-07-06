package com.boluozhai.snow.util;

public final class SystemAttributes {

	private SystemAttributes() {
	}

	public static String get(String key) {
		return get(key, true);
	}

	public static String get(String key, boolean required) {
		String value = System.getenv(key);
		if ((value == null) && (required)) {
			String msg = "need a env-var in java.lang.System: " + key;
			throw new RuntimeException(msg);
		}
		return value;
	}

}
