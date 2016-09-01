package com.boluozhai.snowflake.diskman.impl;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.diskman.DiskManager;
import com.boluozhai.snowflake.diskman.model.FsTable;

public final class DefaultDiskManager implements DiskManager {

	private FsTable _cache_fstab;
	private final SnowflakeContext _context;

	private DefaultDiskManager(SnowflakeContext context) {
		this._context = context;
	}

	public static DiskManager newInstance(SnowflakeContext context) {
		return new DefaultDiskManager(context);
	}

	@Override
	public FsTable getFsTable() {
		FsTable fstab = this._cache_fstab;
		if (fstab == null) {
			fstab = this.loadFsTable();
			this._cache_fstab = fstab;
		}
		return fstab;
	}

	@Override
	public FsTable loadFsTable() {
		InnerFsTableLoader loader = new InnerFsTableLoader(this._context);
		FsTable fstab = loader.load();
		this._cache_fstab = fstab;
		return fstab;
	}

}
