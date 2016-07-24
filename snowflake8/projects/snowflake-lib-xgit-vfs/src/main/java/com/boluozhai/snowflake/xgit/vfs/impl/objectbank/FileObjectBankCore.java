package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;
import com.boluozhai.snowflake.xgit.vfs.TemporaryFileManager;

final class FileObjectBankCore {

	private final SnowContext _context;

	private HashAlgorithmProvider _hash_algorithm_pro;

	private TemporaryFileManager _temp_file_man;

	public FileObjectBankCore(SnowContext context) {
		this._context = context;
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

	public SnowContext getContext() {
		return this._context;
	}

}
