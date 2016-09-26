package com.boluozhai.snowflake.rest.path;

public class PathPattern {

	private final PathPart[] parts;

	private PathPattern(PathPart[] init) {
		this.parts = init;
	}

	public PathPart[] getParts() {
		return parts;
	}

	public static PathPattern parse(String s) {
		PathPart[] pps = PathPatternParser.parse(s);
		return new PathPattern(pps);
	}

}
