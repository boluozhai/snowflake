package com.boluozhai.snow.discovery.utils;

import java.util.HashMap;
import java.util.Map;

public class DiscoveryMapBuilder {

	private Map<String, String> map;
	private final StringBuilder sb;

	public DiscoveryMapBuilder() {
		this.reset();
		this.sb = new StringBuilder();
	}

	public Map<String, String> create() {
		return map;
	}

	public void reset() {
		this.map = new HashMap<String, String>();
		this.sb.setLength(0);
	}

	public void newLine() {
		final int len = sb.length();
		if (len > 0) {
			final String line = sb.toString();
			sb.setLength(0);
			int i = line.indexOf('=');
			if (i > 0) {
				String s1 = line.substring(0, i).trim();
				String s2 = line.substring(i + 1).trim();
				this.map.put(s1, s2);
			}
		}
	}

	public void append(char ch) {
		sb.append(ch);
	}

}
