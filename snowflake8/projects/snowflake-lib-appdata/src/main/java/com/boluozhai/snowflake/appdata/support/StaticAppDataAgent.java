package com.boluozhai.snowflake.appdata.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.boluozhai.snow.util.IOTools;
import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.appdata.AppDataAgent;

public class StaticAppDataAgent extends AppDataAgent {

	private static InnerAppData s_inner;

	private class InnerAppData implements AppData {

		public Exception _error;
		private File _code_path;
		private File _properties_path;
		private File _app_data_path;

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
			return this._app_data_path;
		}

		@Override
		public File getDataSchemaPath(Class<?> clazz) {
			File base = this._app_data_path;
			if (base == null) {
				return null;
			} else {
				String name = clazz.getName();
				return new File(base, name);
			}
		}

		public void init(boolean do_throw) {

			InputStream in = null;

			try {

				// code path

				final URL loc = this.getClass().getProtectionDomain()
						.getCodeSource().getLocation();
				final File code_path = new File(loc.toURI());
				this._code_path = code_path;

				// snow.properties

				final String prop_file_name = "snow.properties";
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

				// data base path
				final String data_base_path_key = "app_data_path";
				final String app_data_path = pro
						.getProperty(data_base_path_key);
				if (app_data_path == null) {
					String msg = "cannot find key in file: key="
							+ data_base_path_key + ", file=" + pro_file;
					throw new RuntimeException(msg);
				} else {
					this._app_data_path = new File(app_data_path);
				}

			} catch (Exception e) {

				this._error = e;

				final RuntimeException re;
				if (e instanceof RuntimeException) {
					re = (RuntimeException) e;
				} else {
					re = new RuntimeException(e);
				}

				if (do_throw) {
					throw re;
				} else {
					System.err.println(e);
				}

			} finally {
				IOTools.close(in);
			}

		}

		@Override
		public Exception getError() {
			return this._error;
		}

	}

	private class FacadeAppData implements AppData {

		private final File _code_path;
		private final File _properties_path;
		private final AppData _target;

		public FacadeAppData(AppData target) {
			this._code_path = target.getCodePath();
			this._properties_path = target.getPropertiesPath();
			this._target = target;
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
			return this._target.getDataBasePath();
		}

		@Override
		public File getDataSchemaPath(Class<?> clazz) {
			return this._target.getDataSchemaPath(clazz);
		}

		@Override
		public Exception getError() {
			return this._target.getError();
		}
	}

	@Override
	public AppData getAppData(boolean throw_exception) {
		InnerAppData inner = s_inner;
		if (inner == null) {
			inner = new InnerAppData();
			inner.init(throw_exception);
			if (inner._error == null) {
				s_inner = inner;
			}
		}
		return new FacadeAppData(inner);
	}

	@Override
	public AppData getAppData() {
		return this.getAppData(true);
	}

}
