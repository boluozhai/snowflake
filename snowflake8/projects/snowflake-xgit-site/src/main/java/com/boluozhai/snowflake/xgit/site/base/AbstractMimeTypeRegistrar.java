package com.boluozhai.snowflake.xgit.site.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.site.MimeTypeInfo;
import com.boluozhai.snowflake.xgit.site.MimeTypeRegistrar;
import com.boluozhai.snowflake.xgit.site.MimeTypeSet;

public class AbstractMimeTypeRegistrar {

	private final Map<String, MimeTypeInfo> table;
	private final ComponentContext context;

	public AbstractMimeTypeRegistrar(ComponentContext context) {
		final Map<String, MimeTypeInfo> map;
		map = new HashMap<String, MimeTypeInfo>();
		this.table = Collections.synchronizedMap(map);
		this.context = context;
	}

	public MimeTypeRegistrar facade() {
		return new MimeTypeRegistrarFacade(true);
	}

	private class MimeTypeRegistrarCache implements MimeTypeRegistrar {

		private final Map<String, MimeTypeInfo> table;

		public MimeTypeRegistrarCache(boolean safe) {
			Map<String, MimeTypeInfo> map = new HashMap<String, MimeTypeInfo>();
			if (safe) {
				map = Collections.synchronizedMap(map);
			}
			this.table = map;
		}

		@Override
		public String getTypeNameByFileName(String fileName) {
			final String extName;
			final int index = fileName.lastIndexOf('.');
			if (index < 0) {
				extName = "";
			} else {
				extName = fileName.substring(index);
			}
			final MimeTypeInfo info = this.get(extName.toLowerCase());
			return info.getMimeType();
		}

		@Override
		public String getTypeNameByExtName(String extName) {
			MimeTypeInfo info = this.get(extName.toLowerCase());
			return info.getMimeType();
		}

		@Override
		public MimeTypeInfo get(String type) {
			MimeTypeInfo info = this.table.get(type);
			if (info == null) {
				final Map<String, MimeTypeInfo> t2;
				t2 = AbstractMimeTypeRegistrar.this.table;
				info = t2.get(type);
				if (info == null) {
					info = new MimeTypeInfo();
					info.setMimeType("application/x+undefined" + type);
					info.setSuffix(type);
				}
				this.table.put(info.getSuffix(), info);
				this.table.put(info.getMimeType(), info);
			}
			return info;
		}

		@Override
		public void register(MimeTypeInfo info) {
			final String mime = info.getMimeType();
			String suffix = info.getSuffix().toLowerCase();
			if (!suffix.startsWith(".")) {
				suffix = '.' + suffix;
			}
			info.setSuffix(suffix);
			this.table.put(suffix, info);
			this.table.put(mime, info);
			final Map<String, MimeTypeInfo> t2 = AbstractMimeTypeRegistrar.this.table;
			t2.put(suffix, info);
			t2.put(mime, info);
		}

		@Override
		public MimeTypeRegistrar cache() {
			return new MimeTypeRegistrarFacade(false);
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return new MyLife();
		}

		@Override
		public ComponentContext getComponentContext() {
			return AbstractMimeTypeRegistrar.this.context;
		}

	}

	private class MimeTypeRegistrarFacade implements MimeTypeRegistrar {

		private final MimeTypeRegistrar cache;

		private MimeTypeRegistrarFacade(boolean safe) {
			this.cache = new MimeTypeRegistrarCache(safe);
		}

		@Override
		public String getTypeNameByFileName(String fileName) {
			return cache.getTypeNameByFileName(fileName);
		}

		@Override
		public String getTypeNameByExtName(String extName) {
			return cache.getTypeNameByExtName(extName);
		}

		@Override
		public MimeTypeInfo get(String typeName) {
			MimeTypeInfo info = this.cache.get(typeName);
			return new MimeTypeInfo(info);
		}

		@Override
		public void register(MimeTypeInfo info) {
			info = new MimeTypeInfo(info);
			this.cache.register(info);
		}

		@Override
		public MimeTypeRegistrar cache() {
			return this.cache.cache();
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return this.cache.lifecycle();
		}

		@Override
		public ComponentContext getComponentContext() {
			return this.cache.getComponentContext();
		}

	}

	private class MyLife implements ComponentLifecycle {

		@Override
		public void onCreate() {
			AbstractMimeTypeRegistrar.this.load_types();
		}
	}

	public void load_types() {
		final MimeTypeRegistrar facade = this.facade();
		final String[] keys = this.context.getAttributeNames();
		for (String key : keys) {
			final Object bean = context.getAttribute(key);
			if (bean instanceof MimeTypeSet) {
				final MimeTypeSet set = (MimeTypeSet) bean;
				final List<MimeTypeInfo> list = set.getTypes();
				for (MimeTypeInfo info : list) {
					facade.register(info);
				}
				final String msg = "load %s, contains %d types.\n";
				System.out.format(msg, bean, list.size());
			}
		}
	}

}
