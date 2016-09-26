package com.boluozhai.snowflake.rest.server.support;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.path.PathPattern;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfoFactory;

public class DefaultPathInfoFactory implements PathInfoFactory {

	private String inAppPathPattern;
	private PathPattern _pattern;

	public String getInAppPathPattern() {
		return inAppPathPattern;
	}

	public void setInAppPathPattern(String inAppPathPattern) {
		this.inAppPathPattern = inAppPathPattern;
	}

	@Override
	public PathInfo create(HttpServletRequest request) {

		PathPattern pattern = this._pattern;
		if (pattern == null) {
			pattern = PathPattern.parse(this.inAppPathPattern);
			this._pattern = pattern;
		}

		return new DefaultPathInfo(request, pattern);
	}

}
