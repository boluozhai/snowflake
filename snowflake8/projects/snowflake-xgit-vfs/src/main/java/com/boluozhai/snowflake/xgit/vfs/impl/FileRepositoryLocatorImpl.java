package com.boluozhai.snowflake.xgit.vfs.impl;

import java.net.URI;
import java.util.List;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGitRuntimeException;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

final class FileRepositoryLocatorImpl implements RepositoryLocator {

	private final String[] _repo_dir_available_names;

	public FileRepositoryLocatorImpl(RepositoryProfile prof) {
		List<String> list = prof.getAvaliableRepositoryDirectoryNames();
		this._repo_dir_available_names = list.toArray(new String[list.size()]);
	}

	@Override
	public URI locate(SnowflakeContext context, URI uri, RepositoryOption option) {

		VFS vfs = VFS.Factory.getVFS(context);
		final VFile base = vfs.newFile(uri);
		VFile config = this.inner_find_config(base);

		if (option == null) {
			option = new RepositoryOption();
		}

		if (config == null) {
			if (option.throw_exception) {
				String msg = "cannot find repository in path of " + uri;
				throw new XGitRuntimeException(msg);
			} else {
				return null;
			}
		}

		return config.getParentFile().toURI();
	}

	private VFile inner_find_config(VFile base) {
		final String config_name = "config";
		String[] array = this.inner_get_repo_dir_names();
		VFile p = base;
		for (; p != null; p = p.getParentFile()) {
			for (String str : array) {
				final VFile conf = p.child(str).child(config_name);
				if (this.inner_is_config(conf)) {
					return conf;
				}
			}
			final VFile conf = p.child(config_name);
			if (this.inner_is_config(conf)) {
				return conf;
			}
		}
		return null;
	}

	private String[] inner_get_repo_dir_names() {
		return this._repo_dir_available_names;
	}

	private boolean inner_is_config(VFile config) {

		if (!config.exists()) {
			return false;
		}

		if (config.isDirectory()) {
			return false;
		}

		if (config.length() < 10) {
			return false;
		}

		return true;
	}

}
