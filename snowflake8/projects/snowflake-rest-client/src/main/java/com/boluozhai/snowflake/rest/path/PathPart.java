package com.boluozhai.snowflake.rest.path;

public class PathPart {

	public final String[] data;
	public final int offset;
	public final int length;

	public PathPart(String[] elements, int offset, int len) {
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
		final int end = offset + length;
		StringBuilder sb = new StringBuilder();
		for (int i = offset; i < end; i++) {
			String s = data[i];
			if (i == offset) {
				if (prefix != null) {
					sb.append(prefix);
				}
				sb.append(s);
			} else {
				sb.append('/').append(s);
			}
		}
		return sb.toString();
	}

}
