package com.boluozhai.snowflake.xgit.vfs.impl.remotes;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.remotes.Remote;
import com.boluozhai.snowflake.xgit.vfs.FileConfig;
import com.boluozhai.snowflake.xgit.vfs.FileRemotes;

final class RemotesImpl implements FileRemotes {

	private final ComponentContext comp_context;
	private final VFile file;
	private RemotesCache remotes_cache;
	private Config config_cache;

	public RemotesImpl(RemotesBuilderImpl builder, ComponentContext cc) {
		this.file = builder.getPath().file();
		this.comp_context = cc;
	}

	private Config inner_get_config() {
		Config conf = this.config_cache;
		if (conf == null) {
			String key = XGitContext.component.config;
			conf = (Config) this.comp_context.getAttribute(key);
			this.config_cache = conf;
		}
		return conf;
	}

	private Map<String, Remote> inner_get_remotes_in_config() {
		RemotesCache cache = this.remotes_cache;
		if (cache != null) {
			if (cache.expired()) {
				cache = null;
			}
		}
		if (cache == null) {
			Config conf = this.inner_get_config();
			cache = RemotesCache.create(conf);
		}
		return cache.data;
	}

	private static class RemotesCache {

		private final Map<String, Remote> data;
		private final long last_mod;
		private final VFile file;

		public RemotesCache(VFile monitor_target, long time,
				Map<String, Remote> table) {
			this.file = monitor_target;
			this.data = table;
			this.last_mod = time;
		}

		public boolean expired() {
			return (file.lastModified() != last_mod);
		}

		public static RemotesCache create(Config conf) {

			try {
				FileConfig file_conf = (FileConfig) conf;
				VFile target = file_conf.getFile();
				long time = target.lastModified();
				Map<String, Remote> tab = RemoteIO.load(conf, false);
				return new RemotesCache(target, time, tab);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				// NOP
			}

		}

	}

	@Override
	public void put(String name, Remote remote) {
		try {
			this.inner_check_name(name);
			remote.setName(name);
			final Remote r0 = this.get(name);
			if (r0 != null) {
				remote.setXgit(r0.isXgit());
			}
			Map<String, Remote> tab = new HashMap<String, Remote>();
			tab.put(remote.getName(), remote);
			if (remote.isXgit()) {
				// to xgit.remotes/file
				VFile ch = this.file.child(name);
				RemoteIO.save(tab, ch);
			} else {
				// to config
				Config conf = this.inner_get_config();
				RemoteIO.save(tab, conf);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// NOP
		}
	}

	@Override
	public Remote get(String name) {
		try {
			this.inner_check_name(name);
			VFile ch = file.child(name);
			if (ch.exists()) {
				// from xgit.remotes/file
				Map<String, Remote> tab = RemoteIO.load(ch, true);
				return tab.get(name);
			} else {
				// from config
				Map<String, Remote> tab = this.inner_get_remotes_in_config();
				return tab.get(name);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// NOP
		}
	}

	private void inner_check_name(String name) {
		if (name == null) {
			throw new SnowflakeException("the remote name cannot be null.");
		} else {
			boolean i1 = name.indexOf('\\') < 0;
			boolean i2 = name.indexOf('/') < 0;
			boolean i3 = name.indexOf('.') < 0;
			if (i1 && i2 && i3) {
				// ok
			} else {
				throw new SnowflakeException("bad remote name : " + name);
			}
		}
	}

	@Override
	public String[] list() {
		Set<String> keys = new HashSet<String>();
		// in config
		Map<String, Remote> tab = this.inner_get_remotes_in_config();
		keys.addAll(tab.keySet());
		// in xgit.remotes directory
		if (file.exists() && file.isDirectory()) {
			String[] li = file.list();
			for (String s : li) {
				keys.add(s);
			}
		}
		// to array
		return keys.toArray(new String[keys.size()]);
	}

	@Override
	public VFile getFile() {
		return this.file;
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new MyLife();
	}

	private class MyLife implements ComponentLifecycle {

		@Override
		public void onCreate() {
			// NOP
		}
	}

	@Override
	public ComponentContext getComponentContext() {
		return this.comp_context;
	}

}
