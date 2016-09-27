package com.boluozhai.snowflake.rest.server.support.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.rest.path.PathPart;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;

public class PathNameExpression implements RestHandlerSwitchExpression {

	private String _path_part_name;

	public PathNameExpression(String pp_name) {
		this._path_part_name = pp_name;
	}

	@Override
	public String getValue(HttpServletRequest request) throws ServletException,
			IOException {

		RestRequestInfo info = RestRequestInfo.Factory.getInstance(request);
		PathInfo path_info = info.getPathInfo();
		PathPart pp = path_info.getPart(this._path_part_name, true);

		return pp.toString();
	}

}
