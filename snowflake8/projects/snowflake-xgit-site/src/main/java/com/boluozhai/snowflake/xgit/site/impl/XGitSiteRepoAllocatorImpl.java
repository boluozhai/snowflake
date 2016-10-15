package com.boluozhai.snowflake.xgit.site.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.site.RepositorySpaceAllocator;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;

final class XGitSiteRepoAllocatorImpl implements RepositorySpaceAllocator {

	private final XGitSite _site;

	public XGitSiteRepoAllocatorImpl(XGitSite site) {
		this._site = site;
	}

	@Override
	public URI allocate(Map<String, String> param) {

		// TODO Auto-generated method stub

		// system
		SystemRepository sys = _site.getSystemRepository();
		ComponentContext context = sys.getComponentContext();
		final URI repo_uri = context.getURI();

		// system.base
		final VFS vfs = VFS.Factory.getVFS(context);
		final VFile repo_path = vfs.newFile(repo_uri);
		final VFile base = repo_path.getParentFile().getParentFile();

		Helper helper = new Helper(base, param);
		String type = helper.getRequiredParam(PARAM.repo_type);

		// types
		if (type == null) {
			// err
		} else if (type.equals(TYPE.user)) {
			return helper.alloc_user_repo();
		} else if (type.equals(TYPE.data)) {
			return helper.alloc_data_repo();
		} else {
			// err
		}

		throw new SnowflakeException("unsupported type: " + type);
	}

	private static class Helper {

		private final VFile _base;
		private final Map<String, String> _param;

		public Helper(VFile base, Map<String, String> param) {
			if (param == null) {
				param = new HashMap<String, String>();
			}
			this._base = base;
			this._param = param;
		}

		private String getRequiredParam(String name) {
			String value = this._param.get(name);
			if (value == null) {
				throw new SnowflakeException("no required param: " + name);
			}
			if (value.length() <= 1) {
				throw new SnowflakeException("bad value: " + value);
			}
			return value;
		}

		public URI alloc_user_repo() {
			String type = "users";
			String user = this.getRequiredParam(PARAM.owner_uid);
			String repo = this.getRequiredParam(PARAM.repo_name);
			VFile file = this._base.child(type).child(user).child(repo);
			return file.toURI();
		}

		public URI alloc_data_repo() {
			// TODO Auto-generated method stub
			throw new SnowflakeException("no impl");
		}
	}

}
