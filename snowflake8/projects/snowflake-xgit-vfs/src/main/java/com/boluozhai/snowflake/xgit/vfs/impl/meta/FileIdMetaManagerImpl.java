package com.boluozhai.snowflake.xgit.vfs.impl.meta;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.HashId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.meta.IdMeta;
import com.boluozhai.snowflake.xgit.meta.IdMetaManager;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;
import com.boluozhai.snowflake.xgit.vfs.HashPathMapper;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

public class FileIdMetaManagerImpl implements Component, IdMetaManager {

	private class MyLife implements ComponentLifecycle {

		@Override
		public void onCreate() {

			FileIdMetaManagerImpl self = FileIdMetaManagerImpl.this;
			ComponentContext cc = self._com_context;

			self._vfs_io = VFSIO.Agent.getInstance(cc);

			self._hash_alg_provider = cc.getBean(
					XGitContext.component.hash_algorithm,
					HashAlgorithmProvider.class);

			self._hash_path_mapper = cc.getBean(
					XGitContext.component.hash_path_mapper,
					HashPathMapper.class);

		}

	}

	private final ComponentContext _com_context;
	private final VPath _path;

	private VFSIO _vfs_io;
	private HashPathMapper _hash_path_mapper;
	private HashAlgorithmProvider _hash_alg_provider;

	public FileIdMetaManagerImpl(FileXGitComponentBuilder builder,
			ComponentContext cc) {

		this._com_context = cc;
		this._path = builder.getPath();

	}

	@Override
	public ComponentContext getComponentContext() {
		return this._com_context;
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new MyLife();
	}

	@Override
	public IdMeta getMeta(Class<?> type, HashId id) {

		FileIdMetaImpl.Builder builder = new FileIdMetaImpl.Builder();

		builder.file = this.inner_make_meta_file(type, id);
		builder.io = this._vfs_io;
		builder.key = type + "@@" + id;
		builder.manager = this;
		builder.type = type;
		builder.id = id;

		return builder.create();

	}

	private VFile inner_make_meta_file(Class<?> type, HashId id) {
		String key = type.getName() + ":" + id.toString();
		HashId hash = this.inner_make_hash(key);
		VFile base = this._path.file();
		HashPathMapper hpm = this._hash_path_mapper;
		return hpm.getHashPath(base, hash, ".id-meta");
	}

	private HashId inner_make_hash(String key) {
		try {
			final String enc = "utf-8";
			byte[] ba = key.getBytes(enc);
			final HashAlgorithmProvider hap = this._hash_alg_provider;
			final MessageDigest md = hap.getMessageDigest();
			ba = md.digest(ba);
			return HashId.Factory.create(ba);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} finally {
			// NOP
		}
	}

}
