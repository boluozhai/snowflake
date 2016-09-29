package com.boluozhai.snowflake.rest.server.impl;

import javax.servlet.http.HttpServletRequest;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfo;
import com.boluozhai.snowflake.rest.server.info.RestRequestInfoFactory;
import com.boluozhai.snowflake.rest.server.info.path.PathInfo;
import com.boluozhai.snowflake.rest.server.info.path.PathInfoFactory;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfo;
import com.boluozhai.snowflake.rest.server.info.session.SessionInfoFactory;
import com.boluozhai.snowflake.rest.server.support.AbstractRestRequestInfoFactory;

final class RestRequestInfoImpl implements RestRequestInfo {

	private final HttpServletRequest _request;
	private final SnowflakeContext _context;
	private final Builder _builder;

	private SessionInfo _session_info;
	private PathInfo _path_info;

	public static final class Builder implements RestRequestInfoFactory {

		private final PathInfoFactory _path_factory;
		private final SessionInfoFactory _session_factory;

		public Builder(AbstractRestRequestInfoFactory abs) {
			this._path_factory = abs.getPathInfoFactory();
			this._session_factory = abs.getSessionInfoFactory();
		}

		@Override
		public RestRequestInfo getInstance(HttpServletRequest request) {
			SnowflakeContext context = WebContextUtils.getWebContext(request);
			return new RestRequestInfoImpl(this, context, request);
		}

		public PathInfo make_path_info(RestRequestInfoImpl target) {
			PathInfoFactory factory = this._path_factory;
			return factory.create(target._request);
		}

		public SessionInfo make_session_info(RestRequestInfoImpl target) {
			SessionInfoFactory factory = this._session_factory;
			return factory.create(target._request);
		}
	}

	public RestRequestInfoImpl(Builder builder, SnowflakeContext context,
			HttpServletRequest request) {

		this._builder = builder;
		this._context = context;
		this._request = request;

	}

	@Override
	public HttpServletRequest getRequest() {
		return this._request;
	}

	@Override
	public SnowflakeContext getContext() {
		return this._context;
	}

	@Override
	public SessionInfo getSessionInfo() {
		SessionInfo info = this._session_info;
		if (info == null) {
			info = this._builder.make_session_info(this);
			this._session_info = info;
		}
		return info;
	}

	@Override
	public PathInfo getPathInfo() {
		PathInfo info = this._path_info;
		if (info == null) {
			info = this._builder.make_path_info(this);
			this._path_info = info;
		}
		return info;
	}

}
