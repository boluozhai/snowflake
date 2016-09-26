package com.boluozhai.snowflake.h2o.rest.helper;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;

public class H2oRestInfo {

	private final PathInfo _path_info;
	private final RestRequestInfo _req_info;

	public static H2oRestInfo getInstance(HttpServletRequest request) {
		RestRequestInfo inner = RestRequestInfo.Factory.getInstance(request);
		return new H2oRestInfo(inner);
	}

	private H2oRestInfo(RestRequestInfo reqinfo) {
		this._req_info = reqinfo;
		this._path_info = reqinfo.getPathInfo();
	}

	public RestRequestInfo getRequestInfo() {
		return this._req_info;
	}

	public PathInfo getPathInfo() {
		return this._path_info;
	}

	public PathPart getId() {
		return this._path_info.getPart("id");
	}

	public String getType() {
		return this._path_info.getPart("type").toString();
	}

}
