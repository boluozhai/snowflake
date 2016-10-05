package com.boluozhai.snowflake.rest.path;

public class PathPattern {

	private final PathPatternPart[] parts;

	private PathPattern(PathPatternPart[] init) {
		this.parts = init;
	}

	public PathPatternPart[] getParts() {
		return parts;
	}

	public static PathPattern parse(String s) {
		PathPatternPart[] pps = PathPatternParser.parse(s);
		return new PathPattern(pps);
	}

	public int length() {
		return this.parts.length;
	}

}
