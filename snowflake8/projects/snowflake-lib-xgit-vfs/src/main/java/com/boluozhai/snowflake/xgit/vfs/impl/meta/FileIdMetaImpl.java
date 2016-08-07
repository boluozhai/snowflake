package com.boluozhai.snowflake.xgit.vfs.impl.meta;

import com.boluozhai.snowflake.xgit.HashId;
import com.boluozhai.snowflake.xgit.meta.IdMeta;
import com.boluozhai.snowflake.xgit.meta.IdMetaManager;

public class FileIdMetaImpl extends FileBaseMeta implements IdMeta {

	public static class Builder extends BaseBuilder {

		public IdMetaManager manager;
		public HashId id;

		public IdMeta create() {
			return new FileIdMetaImpl(this);
		}

	}

	private final IdMetaManager _manager;
	private final HashId _id;

	private FileIdMetaImpl(Builder builder) {
		super(builder);
		this._manager = builder.manager;
		this._id = builder.id;
	}

	@Override
	public HashId getId() {
		return this._id;
	}

	@Override
	public IdMetaManager getManager() {
		return this._manager;
	}

}
