package com.boluozhai.snowflake.xgit.vfs.impl.meta;

import java.net.URI;

import com.boluozhai.snowflake.xgit.meta.UriMeta;
import com.boluozhai.snowflake.xgit.meta.UriMetaManager;

public class FileUriMetaImpl extends FileBaseMeta implements UriMeta {

	public static class Builder extends BaseBuilder {

		public UriMetaManager manager;
		public URI uri;

		public UriMeta create() {
			return new FileUriMetaImpl(this);
		}

	}

	private final UriMetaManager _manager;
	private final URI _uri;

	private FileUriMetaImpl(Builder builder) {
		super(builder);
		this._manager = builder.manager;
		this._uri = builder.uri;
	}

	@Override
	public URI getURI() {
		return this._uri;
	}

	@Override
	public UriMetaManager getManager() {
		return this._manager;
	}

}
