package com.boluozhai.snowflake.datatable.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.DataClientConfiguration;
import com.boluozhai.snowflake.datatable.DataClientFactory;
import com.boluozhai.snowflake.datatable.DataLine;
import com.boluozhai.snowflake.datatable.DataSource;
import com.boluozhai.snowflake.datatable.DataTableDriver;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.datatable.mapping.TypeMapping;
import com.boluozhai.snowflake.datatable.mapping.TypeMappingFactory;
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

/******
 * use 'this' as the HOST while the name is a local username.
 * */

// mapping path : 'ref->object->id->meta'
// refs pattern : 'xgit.private_refs/datatable/user@this'
// the URI pattern is 'datatable://user@host/type'
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
		private TypeMapping types;

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

	private static class InnerClient implements DataClient {

		private final InnerConfig config;
		private ObjectBank objects;
		private RefManager private_refs;
		private HashPathMapper path_mapper;
		private VPath data_table_base;
		public VFSIO vfsio;

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

			ComponentContext cc = repo.getComponentContext();
			ObjectBank objects = (ObjectBank) cc
					.getBean(XGitContext.component.objects);
			RefManager private_refs = (RefManager) cc
					.getBean(XGitContext.component.private_refs);
			HashPathMapper path_mapper = (HashPathMapper) cc
					.getBean(XGitContext.component.hash_path_mapper);

			FileObjectBank file_objs = (FileObjectBank) objects;
			VPath base = file_objs.getFile().toPath().parent()
					.child("xgit.datatable");

			this.objects = objects;
			this.private_refs = private_refs;
			this.path_mapper = path_mapper;
			this.data_table_base = base;
			this.vfsio = VFSIO.Agent.getInstance(context);

		}

		@Override
		public void close() throws IOException {
			// NOP
		}

		@Override
		public DataLine line(String email) {
			int index = email.indexOf('@');
			String user = email.substring(0, index);
			String host = email.substring(index + 1);
			return this.line(host, user);
		}

		@Override
		public DataLine line(String host, String user) {
			InnerLine in_line = new InnerLine(this, host, user);
			return new LineFacade(in_line);
		}

		@Override
		public String[] list(Class<?> type) {
			String prefix = String.format("%s/datatable/",
					XGitContext.component.private_refs);
			return this.private_refs.list(prefix);
		}

		@Override
		public Transaction beginTransaction() {
			return new TransactionImpl();
		}

	}

	private static class InnerLine implements DataLine {

		private final InnerClient client;
		private final String user;
		private final String host;
		private final String key;
		private final Gson gson;
		private final Ref ref;

		public InnerLine(InnerClient client, String host, String user) {

			if (host == null) {
				host = "this";
			}

			String refname = String.format("%s/datatable/%s@%s",
					XGitContext.component.private_refs, user, host);
			Ref ref = client.private_refs.getReference(refname);

			this.client = client;
			this.user = user;
			this.host = host;
			this.key = user + '@' + host;
			this.gson = new Gson();
			this.ref = ref;

		}

		@Override
		public String getUser() {
			return this.user;
		}

		@Override
		public String getHost() {
			return this.host;
		}

		@Override
		public ObjectId getId() {
			return this.ref.getId();
		}

		@Override
		public <T> T insert(T obj) {
			Class<?> type = obj.getClass();
			ObjectProxy proxy = this.get_obj_proxy(type, true);
			return proxy.insert(obj);
		}

		@Override
		public <T> T insertOrUpdate(T obj) {
			Class<?> type = obj.getClass();
			ObjectProxy proxy = this.get_obj_proxy(type, true);
			return proxy.insert_or_update(obj);
		}

		@Override
		public <T> T update(T obj) {
			Class<? extends Object> type = obj.getClass();
			ObjectProxy proxy = this.get_obj_proxy(type);
			return proxy.update(obj);
		}

		@Override
		public <T> T get(Class<T> type) {
			ObjectProxy proxy = this.get_obj_proxy(type);
			return proxy.get(type);
		}

		@Override
		public boolean exists(Class<?> type) {
			ObjectProxy proxy = this.get_obj_proxy(type);
			return proxy.exists();
		}

		@Override
		public boolean delete(Object obj) {
			ObjectProxy proxy = this.get_obj_proxy(obj.getClass());
			return proxy.delete();
		}

		private ObjectProxy get_obj_proxy(Class<?> type) {
			return this.get_obj_proxy(type, false);
		}

		private ObjectProxy get_obj_proxy(Class<?> type, boolean create) {

			ObjectId id = this.get_obj_id(create);
			if (id == null) {
				String msg = "the datatable object not exists, key=" + this.key;
				throw new SnowflakeException(msg);
			}

			VFile base = this.client.data_table_base.file();
			String type_name = this.client.config.types.getName(type);

			if (type_name == null) {
				String msg = "undefine datatable type: " + type;
				throw new SnowflakeException(msg);
			}

			VFile path = this.client.path_mapper.getHashPath(base, id, "."
					+ type_name);
			return new ObjectProxy(this, path);
		}

		private ObjectId get_obj_id(boolean create) {

			ObjectId id = this.get_obj_id();
			if (id != null) {
				return id;
			} else if (create) {
				// next
			} else {
				return null;
			}
			try {
				String enc = "utf-8";
				byte[] ba = this.key.getBytes(enc);
				GitObjectBuilder builder = this.client.objects.newBuilder(
						"datatable", ba.length);
				builder.write(ba, 0, ba.length);
				GitObject obj = builder.create();
				id = obj.id();
				ref.setId(id);
				return id;
			} catch (IOException e) {
				throw new SnowflakeException(e);

			} finally {
			}

		}

		private ObjectId get_obj_id() {
			if (ref.exists()) {
				return ref.getId();
			} else {
				return null;
			}
		}

	}

	private static class ObjectProxy {

		private final VFile file;
		private final InnerLine line;

		private ObjectProxy(InnerLine line, VFile path) {
			this.file = path;
			this.line = line;
		}

		public boolean exists() {
			return file.exists();
		}

		public boolean delete() {
			return file.delete();
		}

		public <T> T get(Class<T> type) {
			if (!this.exists()) {
				String msg = "the object NOT exists: " + this.file;
				throw new SnowflakeException(msg);
			}
			String str = this.readString();
			Gson gs = this.line.gson;
			return gs.fromJson(str, type);
		}

		public <T> T update(T obj) {
			if (!this.exists()) {
				String msg = "the object NOT exists: " + this.file;
				throw new SnowflakeException(msg);
			}
			Gson gs = this.line.gson;
			String str = gs.toJson(obj);
			this.writeString(str);
			return obj;
		}

		public <T> T insert_or_update(T obj) {
			Gson gs = this.line.gson;
			String str = gs.toJson(obj);
			this.writeString(str);
			return obj;
		}

		public <T> T insert(T obj) {
			if (this.exists()) {
				String msg = "the object exists: " + this.file;
				throw new SnowflakeException(msg);
			}
			Gson gs = this.line.gson;
			String str = gs.toJson(obj);
			this.writeString(str);
			return obj;
		}

		private void writeString(String str) {
			OutputStream out = null;
			try {
				VFSIO io = this.line.client.vfsio;
				out = io.output(file, true);
				TextTools.save(str, out);
			} catch (IOException e) {
				throw new SnowflakeException(e);
			} finally {
				IOTools.close(out);
			}
		}

		private String readString() {
			InputStream in = null;
			try {
				VFSIO io = this.line.client.vfsio;
				in = io.input(file);
				return TextTools.load(in);
			} catch (IOException e) {
				throw new SnowflakeException(e);
			} finally {
				IOTools.close(in);
			}
		}

	}

}
