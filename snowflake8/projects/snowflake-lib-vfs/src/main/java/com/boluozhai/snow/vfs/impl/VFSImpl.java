package com.boluozhai.snow.vfs.impl;

import java.io.File;
import java.net.URI;

import com.boluozhai.snow.vfs.VFS;
import com.boluozhai.snow.vfs.VFSContext;
import com.boluozhai.snow.vfs.VFile;
import com.boluozhai.snowflake.context.SnowContext;

final class VFSImpl implements VFS {

	private final VFSContext _context;

	public VFSImpl(SnowContext context) {
		this._context = new VFSContextImpl(context);
	}

	@Override
	public VFSContext context() {
		return _context;
	}

	@Override
	public VFile newFile(String path) {
		File file = new File(path);
		return FileWrapper.wrap(this, file);
	}

	@Override
	public VFile newFile(String dir, String name) {
		File file = new File(dir, name);
		return FileWrapper.wrap(this, file);
	}

	@Override
	public VFile newFile(VFile dir, String name) {
		return dir.child(name);
	}

	@Override
	public VFile newFile(URI uri) {
		File file = new File(uri);
		return FileWrapper.wrap(this, file);
	}

	@Override
	public VFile[] listRoots() {
		File[] list = File.listRoots();
		return FileWrapper.wrap(this, list);
	}

}
