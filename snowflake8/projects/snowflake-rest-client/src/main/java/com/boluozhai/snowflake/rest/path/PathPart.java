package com.boluozhai.snowflake.rest.path;

import java.util.ArrayList;
import java.util.List;

public class PathPart {

	public final String[] data;
	public final int offset;
	public final int length;

	public PathPart(String[] elements) {
		this.data = elements;
		this.offset = 0;
		this.length = elements.length;
	}

	public PathPart(String[] elements, int offset, int len) {

		if (elements.length < offset + len) {
			len = elements.length - offset;
			if (len < 0) {
				len = 0;
			}
		}

		this.data = elements;
		this.offset = offset;
		this.length = len;

	}

	public String[] toArray() {
		String[] array = new String[length];
		for (int i = length - 1; i >= 0; i--) {
			array[i] = data[offset + i];
		}
		return array;
	}

	public PathPart trim() {
		String[] array = this.trimToArray();
		return new PathPart(array);
	}

	public String[] trimToArray() {
		final List<String> list = new ArrayList<String>();
		final int end = this.offset + this.length;
		for (int i = this.offset; i < end; i++) {
			String part = this.data[i];
			if (part == null) {
				// NOP
			} else if (part.equals("")) {
				// NOP
			} else if (part.equals("~")) {
				list.clear();
			} else if (part.equals(".")) {
				// NOP
			} else if (part.equals("..")) {
				list.remove(list.size() - 1);
			} else {
				list.add(part);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public String toString() {
		return this.toString(null);
	}

	public String toString(String prefix) {

		StringBuilder sb = new StringBuilder();
		if (prefix != null) {
			sb.append(prefix);
		}
		final int end = offset + length;
		for (int i = offset; i < end; i++) {
			final String s = data[i];
			if (sb.length() == 0) {
				sb.append(s);
			} else {
				sb.append('/').append(s);
			}
		}
		return sb.toString();

	}

}
