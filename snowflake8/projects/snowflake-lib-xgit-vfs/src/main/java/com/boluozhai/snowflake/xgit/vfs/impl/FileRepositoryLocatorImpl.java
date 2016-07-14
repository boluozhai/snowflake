package com.boluozhai.snowflake.xgit.vfs.impl;

import java.net.URI;

import com.boluozhai.snow.vfs.VFS;
import com.boluozhai.snow.vfs.VFile;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.XGitRuntimeException;
import com.boluozhai.snowflake.xgit.repository.RepositoryLocator;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;

final class FileRepositoryLocatorImpl implements RepositoryLocator {

	@Override
	public URI locate(SnowContext context, URI uri, RepositoryOption option) {

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
		String[] array = { ".git", ".xgit", ".snow", ".snowflake" };
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
