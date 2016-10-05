package com.boluozhai.snowflake.rest.path;

public class PathPatternPart extends PathPart {

	public final boolean mutableLength;

	public PathPatternPart(String[] elements, boolean mut_len) {
		super(elements);
		this.mutableLength = mut_len;
	}

	public PathPatternPart(String[] elements, int offset, int len,
			boolean mut_len) {
		super(elements, offset, len);
		this.mutableLength = mut_len;
	}

}
