package com.boluozhai.snowflake.vfs.impl;

import java.io.File;
import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFSContext;
import com.boluozhai.snowflake.vfs.VFile;

final class VFSImpl implements VFS {

	private final VFSContext _context;

	public VFSImpl(SnowflakeContext context) {
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

	@Override
	public String separator() {
		return File.separator;
	}

	@Override
	public char separatorChar() {
		return File.separatorChar;
	}

	@Override
	public String pathSeparator() {
		return File.pathSeparator;
	}

	@Override
	public char pathSeparatorChar() {
		return File.pathSeparatorChar;
	}

}
