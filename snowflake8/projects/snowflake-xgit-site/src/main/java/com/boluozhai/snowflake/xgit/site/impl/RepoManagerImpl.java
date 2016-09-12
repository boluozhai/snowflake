package com.boluozhai.snowflake.xgit.site.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.site.SiteRepositoryManager;
import com.boluozhai.snowflake.xgit.site.pojo.SiteRepoInfo;
import com.boluozhai.snowflake.xgit.site.pojo.SiteRepoTable;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;
import com.google.gson.Gson;

final class RepoManagerImpl implements SiteRepositoryManager {

	private final Inner _inner;

	private RepoManagerImpl(ComponentContext cc, VPath path) {

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
			return new RepoManagerImpl(cc, path);
		}

	}

	private static class Inner {

		private final ComponentContext _context;
		private final VPath _path;
		private SiteRepoTable _doc;
		private boolean _dirty;
		private final VFile _file_repotab;

		public Inner(ComponentContext cc, VPath path) {

			this._context = cc;
			this._path = path;
			this._file_repotab = path.child("repotab").file();

		}

		private void set_dirty() {
			// TODO need better performance
			this._dirty = true;
			this.do_save(this._doc);
		}

		private SiteRepoTable document() {
			SiteRepoTable doc = this._doc;
			if (doc == null) {
				doc = this.do_load();
				this._dirty = false;
				this._doc = doc;
			}
			return doc;
		}

		private SiteRepoTable do_load() {
			InputStream in = null;
			SiteRepoTable result = null;
			try {
				VFSIO io = VFSIO.Agent.getInstance(_context);
				VFile file = this._file_repotab;
				if (file.exists()) {
					in = io.input(file);
					String s = TextTools.load(in);
					Gson gs = new Gson();
					result = gs.fromJson(s, SiteRepoTable.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOTools.close(in);
			}
			if (result == null) {
				result = new SiteRepoTable();
				Map<String, SiteRepoInfo> map = new HashMap<String, SiteRepoInfo>();
				map = Collections.synchronizedMap(map);
				result.setRepositories(map);
			}
			return result;
		}

		private void do_save(SiteRepoTable doc) {

			if (doc == null) {
				doc = this._doc;
			}

			if (doc == null) {
				return;
			}

			try {
				URI uri = this._file_repotab.toURI();
				File file = new File(uri);
				File dir = file.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
				Gson gs = new Gson();
				String s = gs.toJson(doc);
				TextTools.save(s, file);
				this._dirty = false;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// NOP
			}

		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
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
	public SiteRepoInfo get(String id) {
		SiteRepoTable doc = this._inner.document();
		Map<String, SiteRepoInfo> tab = doc.getRepositories();
		SiteRepoInfo info = tab.get(id);
		if (info != null) {
			return info;
		} else {
			throw new RuntimeException("cannot find repo with id: " + id);
		}
	}

	@Override
	public SiteRepoInfo insert(SiteRepoInfo info) {
		SiteRepoTable doc = this._inner.document();
		Map<String, SiteRepoInfo> tab = doc.getRepositories();
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
	public SiteRepoInfo delete(SiteRepoInfo info) {
		SiteRepoTable doc = this._inner.document();
		Map<String, SiteRepoInfo> tab = doc.getRepositories();
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
	public SiteRepoInfo update(SiteRepoInfo info) {
		SiteRepoTable doc = this._inner.document();
		Map<String, SiteRepoInfo> tab = doc.getRepositories();
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
		SiteRepoTable tab = this._inner.document();
		Map<String, SiteRepoInfo> map = tab.getRepositories();
		Set<String> keys = map.keySet();
		return keys.toArray(new String[keys.size()]);
	}

}
