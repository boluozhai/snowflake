package com.boluozhai.snowflake.rest.path;

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
