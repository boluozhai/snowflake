package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataClientConfiguration;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.datatable.DataSource;
import com.boluozhai.snowflake.datatable.DataTableDriver;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingFactory;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingInfo;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingTable;
import com.boluozhai.snowflake.datatable.pojo.Model;
import com.boluozhai.snowflake.datatable.support.DefaultTypeMappingFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.HashPathMapper;
import com.google.gson.Gson;

// mapping path : 'ref->object->id->meta'
// refs pattern : 'xgit.private_refs/datatable/user@this'
// the hash-obj type is 'datatable' , context is email-address

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

	private static class InnerConfig {

		// private final DataClientFactory factory;
		private DataClientConfiguration config;
		private SnowflakeContext context;
		private TypeMappingTable types;

		public InnerConfig(Factory factory) {
			this.config = factory._config;
			this.context = factory._context;
			// this.factory = factory;
		}

		public void load() {

			TypeMappingFactory factory = new DefaultTypeMappingFactory();
			this.types = factory.create(config.getTypes());

		}

	}

	private static class ModelId {

		private final TypeMappingInfo type;
		private final String name;
		private final String _string;

		private ModelId(TypeMappingInfo aType, String aName) {
			this.type = aType;
			this.name = aName;
			this._string = aType.name() + "@" + aName;
		}

		@Override
		public int hashCode() {
			return this._string.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof ModelId) {
				ModelId o2 = (ModelId) obj;
				return this._string.equals(o2._string);
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return this._string;
		}

	}

	private static class ModelHolderBuilder {

		private ModelId id;
		private boolean create;
		private final InnerTrans trans;

		public ModelHolderBuilder(InnerTrans tx) {
			this.trans = tx;
		}

		public ModelHolder create() {

			// make obj-id
			ObjectId objid = trans.client.get_obj_id(id.name, create);
			if (objid == null) {
				return null;
			}

			// make path

			HashPathMapper mapper = trans.client.path_mapper;
			VFile base = trans.client.data_table_base.file();
			String t_name = id.type.name();
			VFile path = mapper.getHashPath(base, objid, "." + t_name);

			// new
			return new ModelHolder(trans, id, path);
		}

	}

	private static class ModelHolder {

		private final InnerTrans trans;
		private final VFile path;
		private final ModelId id;

		private Model model;
		private boolean delete;

		private ModelHolder(InnerTrans aTrans, ModelId aId, VFile aPath) {
			this.id = aId;
			this.trans = aTrans;
			this.path = aPath;
		}

		public void do_commit() {
			if (this.delete) {
				this.path.delete();
			} else {
				this.save();
			}
		}

		private void save() {
			OutputStream out = null;
			try {
				Model md = this.model;
				if (md == null) {
					return;
				}
				Gson gs = this.trans.client.gson;
				String str = gs.toJson(md);
				this.log("save");
				out = this.trans.client.vfsio.output(path, true);
				TextTools.save(str, out);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOTools.close(out);
			}
		}

		private void log(String op) {

			boolean en = false;

			if (en) {
				String name = this.id.name;
				String type = this.id.type.name();
				String msg = "%s.%s [%s:%s]\n";
				System.out.println(String.format(msg, this, op, type, name));
			}

		}

		private void load() {
			InputStream in = null;
			try {
				VFile file = this.path;
				if (!file.exists()) {
					return;
				}
				this.log("load");
				in = this.trans.client.vfsio.input(file);
				String str = TextTools.load(in);
				Gson gs = this.trans.client.gson;
				Class<? extends Model> type = this.id.type.type();
				Model model = gs.fromJson(str, type);
				this.trans.models.put(model, this.id);
				this.model = model;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOTools.close(in);
			}
		}

		public void setModel(Model md) {
			Class<? extends Model> t1 = md.getClass();
			Class<? extends Model> t2 = this.id.type.type();
			if (t1.equals(t2)) {
				this.model = md;
				this.trans.models.put(md, id);
			} else {
				String msg = "the type not match: t1=%s, t2=%s.";
				msg = String.format(msg, t1, t2);
				throw new RuntimeException();
			}
		}

		public void mark_to_save() {
			this.trans.write_cache.put(id, this);
		}

		public void do_lazy_load() {
			if (this.model == null) {
				this.load();
			}
		}

		public void mark_to_delete() {
			this.delete = true;
			this.trans.write_cache.put(id, this);
		}

	}

	private static class InnerTrans implements Transaction {

		private final InnerClient client;

		private final Map<ModelId, ModelHolder> read_cache;
		private final Map<Model, ModelId> models;
		private final Map<ModelId, ModelHolder> write_cache;

		private InnerTrans(InnerClient client) {
			this.client = client;
			this.read_cache = new HashMap<ModelId, ModelHolder>();
			this.write_cache = new HashMap<ModelId, ModelHolder>();
			this.models = new HashMap<Model, ModelId>();
		}

		@Override
		public void begin() {
			this.reset();
		}

		public void reset() {
			this.read_cache.clear();
			this.write_cache.clear();
			this.models.clear();
		}

		@Override
		public void commit() {
			Map<ModelId, ModelHolder> map = this.write_cache;
			for (ModelId key : map.keySet()) {
				ModelHolder mh = this.write_cache.get(key);
				mh.do_commit();
			}
			map.clear();
		}

		public ModelHolder get_holder(String name, Class<? extends Model> type,
				boolean create) {

			TypeMappingInfo t_info = this.client.config.types.get(type);
			ModelId id = new ModelId(t_info, name);
			ModelHolder mh = this.read_cache.get(id);
			if (mh == null) {
				ModelHolderBuilder builder = new ModelHolderBuilder(this);
				builder.create = create;
				builder.id = id;
				mh = builder.create();
				if (mh != null) {
					mh.do_lazy_load();
					this.read_cache.put(id, mh);
				}
			}
			return mh;
		}

		@Override
		public void rollback() {
			this.reset();
		}

	}

	private static class InnerClient implements DataClient {

		private final InnerConfig config;
		private final Gson gson;

		private ObjectBank objects;
		private RefManager private_refs;
		private HashPathMapper path_mapper;
		private VPath data_table_base;
		private VFSIO vfsio;
		private InnerTrans transaction;

		public InnerClient(InnerConfig conf) {
			this.config = conf;
			this.gson = new Gson();
		}

		public void open() {

			SnowflakeContext context = config.context;
			DataClientConfiguration conf = config.config;
			DataSource source = conf.getDataSource();

			URI uri = URI.create(source.getLocation());
			RepositoryOption option = null;
			RepositoryManager rm = XGit.getRepositoryManager(context);
			Repository repo = rm.open(context, uri, option);

			ComponentContext cc = repo.getComponentContext();
			ObjectBank objects = (ObjectBank) cc
					.getBean(XGitContext.component.objects);
			RefManager refs = (RefManager) cc
					.getBean(XGitContext.component.refs);
			HashPathMapper path_mapper = (HashPathMapper) cc
					.getBean(XGitContext.component.hash_path_mapper);

			FileObjectBank file_objs = (FileObjectBank) objects;
			VPath base = file_objs.getFile().toPath().parent()
					.child("xgit.datatable");

			this.objects = objects;
			this.private_refs = refs;
			this.path_mapper = path_mapper;
			this.data_table_base = base;
			this.vfsio = VFSIO.Agent.getInstance(context);
			this.transaction = new InnerTrans(this);

		}

		@Override
		public void close() throws IOException {
			this.objects = null;
			this.private_refs = null;
			this.path_mapper = null;
			this.data_table_base = null;
			this.vfsio = null;
			this.transaction = null;
		}

		@Override
		public Transaction beginTransaction() {
			InnerTrans trans = this.transaction;
			trans.begin();
			return new TransactionImpl(trans);
		}

		@Override
		public <T extends Model> T update(T obj) {
			ModelId id = this.transaction.models.get(obj);
			if (id == null) {
				String msg = "the model is not in transaction: " + obj;
				throw new RuntimeException(msg);
			} else {
				ModelHolder mh = this.transaction.read_cache.get(id);
				mh.setModel(obj);
				mh.mark_to_save();
				return obj;
			}
		}

		@Override
		public boolean delete(Model obj) {
			ModelId id = this.transaction.models.get(obj);
			if (id == null) {
				String msg = "the model is not in transaction: " + obj;
				throw new RuntimeException(msg);
			} else {
				ModelHolder mh = this.transaction.read_cache.get(id);
				mh.mark_to_delete();
				return true;
			}
		}

		@Override
		public <T extends Model> T insert(String name, T obj) {
			Class<? extends Model> type = obj.getClass();
			Model o2 = this.get(name, type);
			if (o2 == null) {
				ModelHolder mh = this.transaction.get_holder(name, type, true);
				mh.setModel(obj);
				mh.mark_to_save();
				return obj;
			} else {
				String msg = "the object[%s|%s] exists.";
				msg = String.format(msg, name, obj);
				throw new SnowflakeException(msg);
			}
		}

		@Override
		public <T extends Model> T insertOrUpdate(String name, T obj) {
			Class<? extends Model> type = obj.getClass();
			ModelHolder mh = this.transaction.get_holder(name, type, true);
			mh.setModel(obj);
			mh.mark_to_save();
			return obj;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Model> T get(String name, Class<T> type) {
			ModelHolder mh = this.transaction.get_holder(name, type, false);
			if (mh == null) {
				return null;
			}
			mh.do_lazy_load();
			Model md = mh.model;
			return (T) md;
		}

		public ObjectId get_obj_id(String name, boolean create) {
			// make ref
			String refname = String.format("%sdatatable/%s",
					RefManager.prefix.xgit_private_refs, name);
			Ref ref = this.private_refs.getReference(refname);
			// get id
			ObjectId id = null;
			if (ref.exists()) {
				id = ref.getId();
			}
			if (id == null && create) {
				id = this.make_obj_id(name);
				ref.setId(id);
			}
			return id;
		}

		private ObjectId make_obj_id(String name) {
			try {
				String enc = "utf-8";
				String type = "datatable";
				byte[] ba = name.getBytes(enc);
				GitObjectBuilder builder = this.objects.newBuilder(type,
						ba.length);
				builder.write(ba, 0, ba.length);
				GitObject obj = builder.create();
				ObjectId id = obj.id();
				return id;
			} catch (IOException e) {
				throw new SnowflakeException(e);
			} finally {
				// NOP
			}
		}

	}

	// end of class InnerClient

}
