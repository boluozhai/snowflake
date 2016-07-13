package com.boluozhai.snowflake.xgit.vfs.base;

import com.boluozhai.snow.mvc.model.ComponentBuilder;
import com.boluozhai.snow.vfs.MutablePathNode;
import com.boluozhai.snow.vfs.VPath;

public abstract class FileRepoComponentBuilder implements MutablePathNode,
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
