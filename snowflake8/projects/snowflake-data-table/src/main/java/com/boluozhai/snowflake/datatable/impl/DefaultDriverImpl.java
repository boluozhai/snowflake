package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DAO;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataClientConfiguration;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.datatable.DataSource;
import com.boluozhai.snowflake.datatable.DataTableDriver;
import com.boluozhai.snowflake.datatable.TableMeta;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.meta.IdMeta;
import com.boluozhai.snowflake.xgit.meta.IdMetaManager;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

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
			Inner inner = new Inner(this);
			inner.open();
			return new Client(inner);
		}

	}

	private static class Inner {

		private final Factory _factory;
		private Transaction _outer_trans;
		private Map<String, TypeInfo> _type_info_table;
		private Repository _repo;
		private RefManager _private_refs;
		private ObjectBank _objects;
		private IdMetaManager _id_meta;

		public Inner(Factory factory) {
			this._factory = factory;
		}

		public void open() {

			SnowflakeContext context = this._factory._context;
			DataClientConfiguration conf = this._factory._config;

			TypeInfoLoader ti_loader = new TypeInfoLoader(conf);
			this._type_info_table = ti_loader.load();

			DataSource ds = conf.getDataSource();
			URI location = URI.create(ds.getLocation());
			RepositoryManager rm = XGit.getRepositoryManager(context);
			Repository repo = rm.open(context, location, null);
			this._repo = repo;

			XGitContext cc = repo.context();
			this._private_refs = (RefManager) cc
					.getAttribute(XGitContext.component.private_refs);
			this._objects = (ObjectBank) cc
					.getAttribute(XGitContext.component.objects);
			this._id_meta = (IdMetaManager) cc
					.getAttribute(XGitContext.component.id_meta);

		}

		public Transaction get_transaction() {
			Transaction tran = this._outer_trans;
			if (tran == null) {
				tran = new NopTransaction();
				this._outer_trans = tran;
			}
			return tran;
		}

		public ObjectId get_id_by_name(String name, Class<?> type) {
			Ref ref = this.get_ref(name, type);
			return ref.getId();
		}

		public ObjectId gen_id_by_name(String name, Class<?> type)
				throws IOException {
			Ref ref = this.get_ref(name, type);
			if (ref.exists()) {
				return ref.getId();
			} else {
				String enc = "UTF-8";
				String ref_name = ref.getName();
				byte[] data = ref_name.getBytes(enc);
				GitObjectBuilder builder = this._objects.newBuilder(
						"datatable", data.length);
				builder.write(data, 0, data.length);
				GitObject obj = builder.create();
				ObjectId id = obj.id();
				ref.setId(id);
				return id;
			}
		}

		public Ref get_ref(String name, Class<?> type) {
			TypeInfo t_info = this._type_info_table.get(type.getName());
			String tab_name = t_info.name;
			String ref_name = "xgit.private_refs/datatable/" + tab_name + '/'
					+ name;
			return this._private_refs.getReference(ref_name);
		}

		public GitObject get_object(ObjectId id) {
			return this._objects.object(id);
		}

		public IdMeta get_meta(ObjectId id, Class<?> type) {
			return this._id_meta.getMeta(type, id);
		}

		public TypeInfo get_type_info(String tab_name) {
			return this._type_info_table.get(tab_name);
		}

		public TypeInfo get_type_info(Class<?> type) {
			return this._type_info_table.get(type.getName());
		}

	}

	private static class NopTransaction implements Transaction {

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

	private static class Client implements DataClient {

		private final Inner inner;

		public Client(Inner in) {
			this.inner = in;
		}

		@Override
		public void close() throws IOException {
			inner._repo = null;
		}

		@Override
		public Transaction beginTransaction() throws SnowflakeException {
			return inner.get_transaction();
		}

		@Override
		public <T> T insert(T obj) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T update(T obj) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> boolean delete(Object obj) {
			Class<?> type = obj.getClass();
			TypeInfo t_info = inner.get_type_info(type);
			ObjectId id = t_info.dao.getId(obj);
			return this.delete(id, type);
		}

		@Override
		public DataClientFactory getFactory() {
			return inner._factory;
		}

		@Override
		public <T> boolean delete(ObjectId id, Class<T> type) {

			GitObject o2 = inner.get_object(id);

			return false;
		}

		@Override
		public <T> T get(ObjectId id, Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T get(ObjectId id, Class<T> type, boolean canBeNil) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T get(String name, Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T get(String name, Class<T> type, boolean canBeNil) {

			Class<?> type2 = type;
	//		ObjectId id = inner.name2id(name, type2);

			if (canBeNil) {
			}

			return null;
		}

	}

	private static class TypeInfo {

		private final DAO dao;
		private final String name;
		private final Class<?> type;

		public TypeInfo(TableMeta meta) {

			DAO dao = meta.getDao();
			String name = meta.getName();
			Object pt = meta.getPrototype();
			Class<?> type = pt.getClass();

			this.dao = dao;
			this.name = name.toLowerCase();
			this.type = type;

		}

		public void registerTo(Map<String, TypeInfo> result) {

			String t_name = this.type.getName();
			result.put(name, this);
			result.put(t_name, this);

		}

	}

	private static class TypeInfoLoader {

		private DataClientConfiguration _config;

		public TypeInfoLoader(DataClientConfiguration conf) {
			this._config = conf;
		}

		public Map<String, TypeInfo> load() {
			Map<String, TypeInfo> result = new HashMap<String, TypeInfo>();
			Map<String, TableMeta> tab = _config.getTables();
			for (String key : tab.keySet()) {
				TableMeta meta = tab.get(key);
				if (meta.getName() == null) {
					meta.setName(key);
				}
				TypeInfo info = new TypeInfo(meta);
				info.registerTo(result);
			}
			return result;
		}
	}

}
