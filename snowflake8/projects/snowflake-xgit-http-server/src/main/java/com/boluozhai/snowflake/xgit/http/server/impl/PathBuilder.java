package com.boluozhai.snowflake.xgit.http.server.impl;

import java.util.ArrayList;
import java.util.List;

final class PathBuilder {

	private List<String> elements;

	public PathBuilder() {
		this.elements = new ArrayList<String>();
	}

	public void appendElements(String s) {

		s = s.replace('\\', '/');
		String[] array = s.split("/");

		for (String item : array) {
			if (item == null) {
				continue;
			}
			item = item.trim();
			if (item.length() == 0) {
				continue;
			}
			this.elements.add(item);
		}

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String s : this.elements) {
			sb.append('/').append(s);
		}
		return sb.toString();
	}

	public String toSubString(int from, int to) {
		StringBuilder sb = new StringBuilder();
		for (int i = from; i < to; i++) {
			String s = this.elements.get(i);
			sb.append('/').append(s);
		}
		return sb.toString();
	}

	public String toSubString(int from) {
		return this.toSubString(from, this.elements.size());
	}

	public String[] toArray() {
		List<String> list = this.elements;
		return list.toArray(new String[list.size()]);
	}

	public void reset() {
		this.elements.clear();
	}

}
