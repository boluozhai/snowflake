package com.boluozhai.snowflake.xgit.vfs.impl.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeProperties;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.vfs.FileConfig;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

public class FileRepoConfigImpl implements FileConfig {

	private static class Builder extends FileXGitComponentBuilder {

		private ContextBuilder __cb__;
		private VPath __path__;
		private Map<String, String> __table__;

		@Override
		public void configure(ContextBuilder cb) {
			this.__cb__ = cb;
			this.try_preload();
		}

		@Override
		public Component create(ComponentContext cc) {
			VFile file = this.getPath().file();
			Map<String, String> init = this.__table__;
			return new FileRepoConfigImpl(cc, file, init);
		}

		@Override
		public void setPath(VPath path) {
			super.setPath(path);
			this.__path__ = path;
			this.try_preload();
		}

		private void try_preload() {
			ContextBuilder cb = this.__cb__;
			VPath path = this.__path__;
			if (cb == null || path == null) {
				return;
			}
			try {
				ConfigLoader loader = new ConfigLoader(cb.getParent());
				Map<String, String> map = loader.load(path.file());
				Set<String> keys = map.keySet();
				for (String key : keys) {
					String value = map.get(key);
					cb.setProperty(key, value);
				}
				this.__table__ = map;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}

	}

	private static class Inner {

		private final Map<String, String> table;
		private final ComponentContext context;
		private final VFile file;

		private Inner(ComponentContext cc, VFile file) {
			this.table = new HashMap<String, String>();
			this.context = cc;
			this.file = file;
		}

		public void load() throws IOException {
			ConfigLoader loader = new ConfigLoader(context);
			Map<String, String> map = loader.load(file);
			table.clear();
			table.putAll(map);
		}

		public void save() throws IOException {
			ConfigSaver saver = new ConfigSaver(context);
			saver.save(table, file);
		}

		public void put(String key, String value) {
			table.put(key, value);
		}

		public String[] keys() {
			Set<String> keys = table.keySet();
			return keys.toArray(new String[keys.size()]);
		}

		public String get(String name) {
			Object def = SnowflakeProperties.exception;
			return this.get(name, def);
		}

		public String get(String key, Object def) {
			String value = table.get(key);
			if (value == null) {
				if (def == null) {
					// NOP
				} else if (def == SnowflakeProperties.exception) {
					String msg = "no value for key: " + key;
					throw new RuntimeException(msg);
				} else {
					value = def.toString();
				}
			}
			return value;
		}

	}

	public static ComponentBuilder newBuilder() {
		return new Builder();
	}

	private final Inner inner;

	private FileRepoConfigImpl(ComponentContext cc, VFile file,
			Map<String, String> init) {
		this.inner = new Inner(cc, file);
		if (init != null) {
			this.inner.table.putAll(init);
		}
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public ComponentContext getComponentContext() {
		return inner.context;
	}

	@Override
	public void setProperty(String key, String value) {
		inner.put(key, value);
	}

	@Override
	public String[] getPropertyNames() {
		return inner.keys();
	}

	@Override
	public String getProperty(String name) {
		return inner.get(name);
	}

	@Override
	public String getProperty(String name, Object defaultValue) {
		return inner.get(name, defaultValue);
	}

	@Override
	public void load() throws IOException {
		inner.load();
	}

	@Override
	public void save() throws IOException {
		inner.save();
	}

	@Override
	public VFile getFile() {
		return inner.file;
	}

}
