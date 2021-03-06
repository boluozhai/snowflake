package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.TemporaryFileManager;

final class FileObjectBankCore {

	public static final int small_object_builder_buffer_size_max;

	static {

		int limit = 1024 * 1024 * 16;
		small_object_builder_buffer_size_max = limit;
		String fmt = "for debug[%s]: now set the max small-obj-size is %d bytes.\n";
		System.err.format(fmt, FileObjectBankCore.class, limit);

	}

	private final SnowflakeContext _context;

	private HashAlgorithmProvider _hash_algorithm_pro;
	private TemporaryFileManager _temp_file_man;
	private FileObjectBank _bank;

	public FileObjectBankCore(SnowflakeContext context) {
		this._context = context;
	}

	public FileObjectBank getObjectBank() {
		FileObjectBank bank = this._bank;
		if (bank == null) {
			String key = XGitContext.component.objects;
			bank = _context.getBean(key, FileObjectBank.class);
			this._bank = bank;
		}
		return bank;
	}

	public TemporaryFileManager getTemporaryFileManager() {
		TemporaryFileManager tfm = this._temp_file_man;
		if (tfm == null) {
			String key = XGitContext.component.temporary_files;
			tfm = _context.getBean(key, TemporaryFileManager.class);
			this._temp_file_man = tfm;
		}
		return tfm;
	}

	public HashAlgorithmProvider getHashAlgorithmProvider() {
		HashAlgorithmProvider hap = this._hash_algorithm_pro;
		if (hap == null) {
			String key = XGitContext.component.hash_algorithm;
			hap = this._context.getBean(key, HashAlgorithmProvider.class);
			this._hash_algorithm_pro = hap;
		}
		return hap;
	}

	public SnowflakeContext getContext() {
		return this._context;
	}

}
