package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataClientConfiguration;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.datatable.DataLine;
import com.boluozhai.snowflake.datatable.DataSource;
import com.boluozhai.snowflake.datatable.DataTableDriver;
import com.boluozhai.snowflake.datatable.TableMeta;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.google.gson.Gson;

final class DefaultDriverImpl implements DataTableDriver {

	@Override
	public DataClientFactory createFactory(SnowflakeContext context,
			DataClientConfiguration conf) {
		return new Factory(context, conf);
	}

	private static class Factory implements DataClientFactory {

		private final DataClientConfiguration _config;
		private final SnowflakeContext _context;

		public Factory(SnowflakeContext context, DataClientConfiguration conf) {
			this._config = conf;
			this._context = context;
		}

		@Override
		public DataClient open() {
			InnerConfig conf = new InnerConfig(this);
			conf.load();
			InnerClient inner = new InnerClient(conf);
			inner.open();
			return new ClientFacade(inner);
		}

	}

	private static class InnerType {

		private String name;
		// private DAO dao;
		private Class<?> clazz;

		public InnerType(String key) {
			this.name = key;
		}

		public void load(TableMeta meta) {
			String name = meta.getName();
			if (name != null) {
				this.name = name;
			}
			// this.dao = meta.getDao();
			this.clazz = meta.getPrototype().getClass();
		}

		public void registerTo(Map<String, InnerType> tab) {

			String k1 = this.name.toUpperCase();
			String k2 = this.clazz.getName();
			tab.put(k1, this);
			tab.put(k2, this);

			this.name = k1;
		}

	}

	private static class InnerConfig {

		private final DataClientFactory factory;
		private DataClientConfiguration config;
		private SnowflakeContext context;
		private Map<String, InnerType> types;

		public InnerConfig(Factory factory) {
			this.config = factory._config;
			this.context = factory._context;
			this.factory = factory;
		}

		public void load() {

			// types
			Map<String, TableMeta> tab = config.getTables();
			Map<String, InnerType> tab2 = new HashMap<String, InnerType>();
			for (String key : tab.keySet()) {
				InnerType type = new InnerType(key);
				type.load(tab.get(key));
				type.registerTo(tab2);
			}
			this.types = Collections.synchronizedMap(tab2);

		}

		public InnerType get_type(String type) {
			InnerType it = this.types.get(type);
			if (it == null) {
				String msg = "cannot find type for name: " + type;
				throw new SnowflakeException(msg);
			}
			return it;
		}

		public InnerType get_type(Class<?> type) {
			return this.get_type(type.getName());
		}

	}

	private static class InnerClient implements DataClient {

		private final InnerConfig config;
		private VFile base_path;
		private VFSIO vfs_io;
		private InnerTrans inner_transaction;

		public InnerClient(InnerConfig conf) {
			this.config = conf;
		}

		public void open() {

			SnowflakeContext context = config.context;
			DataClientConfiguration conf = config.config;
			DataSource source = conf.getDataSource();

			URI uri = URI.create(source.getLocation());
			RepositoryOption option = null;
			RepositoryManager rm = XGit.getRepositoryManager(context);
			Repository repo = rm.open(context, uri, option);

			VFS vfs = VFS.Factory.getVFS(context);
			uri = repo.context().getURI();
			final VFile repo_dir = vfs.newFile(uri);
			this.base_path = repo_dir.child("xgit.datatable");
			this.vfs_io = VFSIO.Agent.getInstance(context);

		}

		@Override
		public void close() throws IOException {
			this.base_path = null;
		}

		@Override
		public Transaction beginTransaction() throws SnowflakeException {
			InnerTrans inner = this.get_inner_transaction();
			inner.begin();
			return new TransactionFacade(inner);
		}

		private InnerTrans get_inner_transaction() {
			InnerTrans inner = this.inner_transaction;
			if (inner == null) {
				inner = new InnerTrans();
				this.inner_transaction = inner;
			}
			return inner;
		}

		@Override
		public DataLine line(String key, String type) {
			key = this.normal_key_name(key);
			InnerType it = config.get_type(type);
			VFile path = this.base_path.child(it.name).child(key);
			InnerLine inner = new InnerLine(this, it, path, key);
			return new LineFacade(inner);
		}

		private String normal_key_name(String key) {
			final char[] chs = key.trim().toLowerCase().toCharArray();
			if (chs.length < 1) {
				throw new SnowflakeException("bad key name: " + key);
			}
			for (int i = chs.length - 1; i >= 0; i--) {
				final char ch = chs[i];
				if ('0' <= ch && ch <= '9') {
					continue;
				} else if ('a' <= ch && ch <= 'z') {
					continue;
				} else {
					chs[i] = '_';
				}
			}
			return new String(chs);
		}

		@Override
		public DataLine line(String key, Class<?> type) {
			return this.line(key, type.getName());
		}

		@Override
		public String[] list(Class<?> type) {

			InnerType it = config.get_type(type);
			VFile dir = this.base_path.child(it.name);
			VFile[] list = dir.listFiles();
			List<String> strlist = new ArrayList<String>();

			for (VFile item : list) {
				if (item.exists()) {
					if (!item.isDirectory()) {
						strlist.add(item.getName());
					}
				}
			}

			return strlist.toArray(new String[strlist.size()]);
		}

		@Override
		public DataClientFactory getFactory() {
			return this.config.factory;
		}

		@Override
		public DataLine line(String key) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class InnerTrans implements Transaction {

		@Override
		public void begin() throws SnowflakeException {
		}

		@Override
		public void commit() throws SnowflakeException {
		}

		@Override
		public void rollback() throws SnowflakeException {
		}

	}

	private static class InnerLine implements DataLine {

		private final VFile path;
		private final InnerClient client;
		private final InnerType type;
		private final String name;
		private final String key;
		private final Gson gson;

		public InnerLine(InnerClient client, InnerType type, VFile path,
				String name) {
			this.type = type;
			this.client = client;
			this.path = path;
			this.name = name;
			this.key = type.name + ':' + name;
			this.gson = new Gson();
		}

		@Override
		public String getKey() {
			return this.key;
		}

		@Override
		public <T> T insert(T obj) {
			if (path.exists()) {
				String msg = "the object [%s:%s] is exists.";
				msg = String.format(msg, type.name, this.name);
				throw new SnowflakeException(msg);
			}
			return this.write_object(obj);
		}

		@Override
		public <T> T update(T obj) {
			if (!path.exists()) {
				String msg = "the object [%s:%s] is not exists.";
				msg = String.format(msg, type.name, this.name);
				throw new SnowflakeException(msg);
			}
			return this.write_object(obj);
		}

		private <T> T write_object(T obj) {
			this.check_type(obj.getClass());
			OutputStream out = null;
			try {
				String str = gson.toJson(obj);
				out = client.vfs_io.output(path, true);
				TextTools.save(str, out);
				return obj;
			} catch (IOException e) {
				throw new SnowflakeException(e);
			} finally {
				IOTools.close(out);
			}
		}

		@Override
		public boolean delete() {
			return path.delete();
		}

		@Override
		public boolean exists() {
			return path.exists();
		}

		@Override
		public <T> T get(Class<T> type) {
			Class<?> t2 = type;
			this.check_type(t2);
			InputStream in = null;
			try {
				in = client.vfs_io.input(path);
				String str = TextTools.load(in);
				return gson.fromJson(str, type);
			} catch (IOException e) {
				throw new SnowflakeException(e);
			} finally {
				IOTools.close(in);
			}
		}

		private void check_type(Class<?> t2) {
			if (!this.type.clazz.equals(t2)) {
				String msg = "bad data type: want=[%s], get=[%s].";
				msg = String.format(msg, type.clazz, t2);
				throw new SnowflakeException(msg);
			}
		}

		@Override
		public Class<?> getTypeClass() {
			return type.clazz;
		}

		@Override
		public String getType() {
			return type.name;
		}

		@Override
		public String getName() {
			return this.name;
		}

	}

}
