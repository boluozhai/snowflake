package com.boluozhai.snowflake.xgit.vfs.base;

import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.vfs.MutablePathNode;
import com.boluozhai.snowflake.vfs.VPath;

public abstract class FileXGitComponentBuilder implements MutablePathNode,
		ComponentBuilder {

	private VPath _path;

	@Override
	public VPath getPath() {
		return this._path;
	}

	@Override
	public void setPath(VPath path) {
		this._path = path;
	}

}
