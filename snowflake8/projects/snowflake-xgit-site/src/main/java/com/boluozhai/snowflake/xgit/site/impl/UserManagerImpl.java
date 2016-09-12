package com.boluozhai.snowflake.xgit.site.impl;

import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.site.SiteUserManager;
import com.boluozhai.snowflake.xgit.site.pojo.SiteUserInfo;
import com.boluozhai.snowflake.xgit.site.pojo.SiteUserTable;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

final class UserManagerImpl implements SiteUserManager, Component {

	private final Inner _inner;

	private UserManagerImpl(ComponentContext cc, VPath path) {
		this._inner = new Inner(cc, path);
	}

	public static ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends FileXGitComponentBuilder implements
			ComponentBuilder {

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {
			VPath path = this.getPath();
			return new UserManagerImpl(cc, path);
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}

	}

	private static class Inner {

		private final ComponentContext _context;
		private final VPath _path;
		private final VFile _file_usertab;
		private SiteUserTable _doc;
		private boolean _dirty;

		public Inner(ComponentContext cc, VPath path) {
			this._context = cc;
			this._path = path;
			this._file_usertab = path.child("usertab").file();
		}

		public SiteUserTable document() {
			SiteUserTable doc = this._doc;
			if (doc == null) {
				doc = this.do_load();
				this._doc = doc;
				this._dirty = false;
			}
			return doc;
		}

		private SiteUserTable do_load() {
			// TODO Auto-generated method stub
			return null;
		}

		private void do_save(SiteUserTable doc) {
			// TODO Auto-generated method stub
		}

		public void set_dirty() {
			// TODO need better performance
			this._dirty = true;
			this.do_save(this._doc);
		}

	}

	@Override
	public ComponentContext getComponentContext() {
		return this._inner._context;
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public SiteUserInfo get(String id) {
		SiteUserTable doc = this._inner.document();
		Map<String, SiteUserInfo> tab = doc.getUsers();
		SiteUserInfo info = tab.get(id);
		if (info != null) {
			return info;
		} else {
			throw new RuntimeException("cannot find user with id: " + id);
		}
	}

	@Override
	public SiteUserInfo insert(SiteUserInfo info) {
		SiteUserTable doc = this._inner.document();
		Map<String, SiteUserInfo> tab = doc.getUsers();
		String id = info.getId();
		if (!tab.containsKey(id)) {
			tab.put(id, info);
			this._inner.set_dirty();
			return info;
		} else {
			throw new RuntimeException("the info is exists, id: " + id);
		}
	}

	@Override
	public SiteUserInfo delete(SiteUserInfo info) {
		SiteUserTable doc = this._inner.document();
		Map<String, SiteUserInfo> tab = doc.getUsers();
		String id = info.getId();
		if (tab.containsKey(id)) {
			info = tab.remove(id);
			this._inner.set_dirty();
			return info;
		} else {
			throw new RuntimeException("cannot delete, no id: " + id);
		}
	}

	@Override
	public SiteUserInfo update(SiteUserInfo info) {
		SiteUserTable doc = this._inner.document();
		Map<String, SiteUserInfo> tab = doc.getUsers();
		String id = info.getId();
		if (tab.containsKey(id)) {
			tab.put(id, info);
			this._inner.set_dirty();
			return info;
		} else {
			throw new RuntimeException("cannot update, no id: " + id);
		}
	}

	@Override
	public String[] ids() {
		SiteUserTable doc = this._inner.document();
		Map<String, SiteUserInfo> tab = doc.getUsers();
		Set<String> keys = tab.keySet();
		return keys.toArray(new String[keys.size()]);
	}

}
