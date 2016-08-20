package com.boluozhai.snowflake.xgit.vfs.impl.meta;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.HashId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.meta.UriMeta;
import com.boluozhai.snowflake.xgit.meta.UriMetaManager;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;
import com.boluozhai.snowflake.xgit.vfs.HashPathMapper;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

public class FileUriMetaManagerImpl implements Component, UriMetaManager {

	private class MyLife implements ComponentLifecycle {

		@Override
		public void onCreate() {

			FileUriMetaManagerImpl self = FileUriMetaManagerImpl.this;
			ComponentContext cc = self._com_context;

			self._vfs_io = VFSIO.Agent.getInstance(cc);

			self._hash_alg_provider = cc.getBean(
					XGitContext.component.hash_algorithm,
					HashAlgorithmProvider.class);

			self._hash_path_mapper = cc.getBean(
					XGitContext.component.hash_path_mapper,
					HashPathMapper.class);

			FileWorkspace workspace = cc.getBean(
					XGitContext.component.workspace, FileWorkspace.class);
			self._wk_base_uri = workspace.getFile().toURI().toString();

		}
	}

	private final ComponentContext _com_context;
	private final VPath _path;

	private VFSIO _vfs_io;
	private HashAlgorithmProvider _hash_alg_provider;
	private HashPathMapper _hash_path_mapper;
	private String _wk_base_uri;

	public FileUriMetaManagerImpl(FileXGitComponentBuilder builder,
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
	public UriMeta getMeta(Class<?> type, URI uri) {

		FileUriMetaImpl.Builder builder = new FileUriMetaImpl.Builder();

		builder.file = this.inner_make_meta_file(type, uri);
		builder.io = this._vfs_io;
		builder.key = type + ":" + uri;
		builder.manager = this;
		builder.type = type;
		builder.uri = uri;

		return builder.create();

	}

	private VFile inner_make_meta_file(Class<?> type, URI uri) {

		final String s1 = this._wk_base_uri;
		final String s2 = uri.toString();
		final String type_name = type.getName();
		final String key;
		if (s2.startsWith(s1)) {
			String p = this.inner_normal_path(s2.substring(s1.length()));
			key = type_name + ":{workspace}:" + p;
		} else {
			String p = this.inner_normal_path(s2);
			key = type_name + ":{fs_root}:" + p;
		}

		HashId hash = this.inner_make_hash(key);
		VFile base = this._path.file();
		HashPathMapper hpm = this._hash_path_mapper;
		return hpm.getHashPath(base, hash, ".uri-meta");

	}

	private String inner_normal_path(String s) {
		final int len0 = s.length();
		int len = len0;
		int off = 0;
		if (s.startsWith("/")) {
			len--;
			off++;
		}
		if (s.endsWith("/")) {
			len--;
		}
		if (len != len0) {
			return s.substring(off, off + len);
		} else {
			return s;
		}
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
