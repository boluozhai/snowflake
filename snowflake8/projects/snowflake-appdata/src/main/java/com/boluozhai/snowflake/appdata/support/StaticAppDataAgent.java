package com.boluozhai.snowflake.appdata.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.appdata.AppDataAgent;
import com.boluozhai.snowflake.context.SnowflakeProperties;
import com.boluozhai.snowflake.util.IOTools;

public class StaticAppDataAgent extends AppDataAgent {

	private static final ADManager s_manager;

	static {
		s_manager = new ADManager();
	}

	private static class ADManager {

		private final Map<String, ADHolder> holders;

		public ADManager() {
			Map<String, ADHolder> map = new HashMap<String, ADHolder>();
			holders = Collections.synchronizedMap(map);
		}

		public ADHolder getHolder(Class<?> clazz) {
			String key = clazz.getName();
			ADHolder holder = holders.get(key);
			if (holder == null) {
				holder = new ADHolder(key, clazz);
				holders.put(key, holder);
			}
			return holder;
		}
	}

	private static class ADHolder {

		private AppData _cache;
		private final Class<?> _target;
		private final String _key;

		public ADHolder(String key, Class<?> clazz) {
			this._target = clazz;
			this._key = key;
		}

		public AppData getAppData(boolean throw_exception) {
			AppData ad = this._cache;
			if (ad == null) {
				ad = this.build();
				Exception err = ad.getError();
				if (err == null) {
					this._cache = ad;
				} else if (throw_exception) {
					this.throw_exception(err);
				}
			}
			return ad;
		}

		private void throw_exception(Exception err) {
			RuntimeException re = null;
			if (err instanceof RuntimeException) {
				re = (RuntimeException) err;
			} else {
				re = new RuntimeException(err);
			}
			throw re;
		}

		private AppData build() {
			InnerAppData ad = new InnerAppData(this);
			ad.init();
			return new FacadeAppData(ad);
		}

	}

	private static class InnerAppData implements AppData {

		private final Class<?> _target;
		private final String _key;

		public Exception _error;

		private File _code_path;
		private File _properties_path;
		private File _data_base_path;
		private File _data_scheme_path;
		private Properties _properties;

		public InnerAppData(ADHolder holder) {
			this._target = holder._target;
			this._key = holder._key;
		}

		public void init() {

			InputStream in = null;

			try {

				// code path

				final URL loc = this._target.getProtectionDomain()
						.getCodeSource().getLocation();
				final File code_path = new File(loc.toURI());
				this._code_path = code_path;

				// snow.properties

				final String prop_file_name = "snowflake.properties";
				File p = code_path;
				for (; p != null; p = p.getParentFile()) {
					final File file = new File(p, prop_file_name);
					if (file.exists() && file.isFile()) {
						p = file;
						break;
					}
				}
				if (p == null) {
					String msg = "cannot find file in path: file="
							+ prop_file_name + ", path=" + code_path;
					throw new RuntimeException(msg);
				} else {
					this._properties_path = p;
				}
				final File pro_file = p;
				in = new FileInputStream(pro_file);
				Properties pro = new Properties();
				pro.load(in);
				this._properties = pro;

				// data base path
				final String data_base_path_key = "app_data_path";
				final String app_data_path = pro
						.getProperty(data_base_path_key);
				if (app_data_path == null) {
					String msg = "cannot find key in file: key="
							+ data_base_path_key + ", file=" + pro_file;
					throw new RuntimeException(msg);
				} else {
					File data_base = new File(app_data_path);
					File data_schema = new File(data_base, this._key);
					this._data_base_path = data_base;
					this._data_scheme_path = data_schema;
				}

			} catch (Exception e) {

				this._error = e;

			} finally {
				IOTools.close(in);
			}

		}

		@Override
		public Exception getError() {
			return this._error;
		}

		@Override
		public Class<?> getTargetClass() {
			return this._target;
		}

		@Override
		public File getCodePath() {
			return this._code_path;
		}

		@Override
		public File getPropertiesPath() {
			return this._properties_path;
		}

		@Override
		public File getDataBasePath() {
			return this._data_base_path;
		}

		@Override
		public File getDataSchemaPath() {
			return this._data_scheme_path;
		}

		@Override
		public String[] getPropertyNames() {
			Set<Object> keys = this._properties.keySet();
			String[] array = keys.toArray(new String[keys.size()]);
			return array;
		}

		@Override
		public String getProperty(String name) {
			return this.getProperty(name, SnowflakeProperties.exception);
		}

		@Override
		public String getProperty(String name, Object defaultValue) {
			String value = this._properties.getProperty(name);
			if (value == null) {
				if (defaultValue == null) {
					// NOP
				} else if (defaultValue == SnowflakeProperties.exception) {
					String msg = "need key:" + name + " in file:"
							+ this._properties_path;
					throw new RuntimeException(msg);
				} else {
					value = defaultValue.toString();
				}
			}
			return value;
		}

	}

	private static class FacadeAppData implements AppData {

		private AppData inner;

		private FacadeAppData(AppData in) {
			this.inner = in;
		}

		public Class<?> getTargetClass() {
			return inner.getTargetClass();
		}

		public File getCodePath() {
			return inner.getCodePath();
		}

		public File getPropertiesPath() {
			return inner.getPropertiesPath();
		}

		public File getDataBasePath() {
			return inner.getDataBasePath();
		}

		public File getDataSchemaPath() {
			return inner.getDataSchemaPath();
		}

		public Exception getError() {
			return inner.getError();
		}

		public String[] getPropertyNames() {
			return inner.getPropertyNames();
		}

		public String getProperty(String name) {
			return inner.getProperty(name);
		}

		public String getProperty(String name, Object defaultValue) {
			return inner.getProperty(name, defaultValue);
		}

	}

	@Override
	public AppData getAppData(Class<?> clazz) {
		return this.getAppData(clazz, true);
	}

	@Override
	public AppData getAppData(Class<?> clazz, boolean throw_exception) {
		ADHolder holder = s_manager.getHolder(clazz);
		return holder.getAppData(throw_exception);
	}

}
