package com.boluozhai.snowflake.h2o.rest.helper;

import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathPart;

public class PathInfoWrapper {

	private final PathInfo _path_info;

	public PathInfoWrapper(RestRequestInfo reqinfo) {
		this._path_info = reqinfo.getPathInfo();
	}

	public PathInfoWrapper(PathInfo pi) {
		this._path_info = pi;
	}

	public PathPart getId() {
		return this._path_info.getPart("id");
	}

	public String getType() {
		return this._path_info.getPart("type").toString();
	}

}
