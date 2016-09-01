package com.boluozhai.snowflake.xgit.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.TreeDAO;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.PlainId;
import com.boluozhai.snowflake.xgit.pojo.TreeItem;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class DefaultTreeDAOFactory {

	public static TreeDAO create(Repository repo) {
		return new MyDAO(repo);
	}

	public static TreeDAO create(ObjectBank repo) {
		return new MyDAO(repo);
	}

	private static class MyDAO implements TreeDAO {

		private final ObjectBank _bank;

		public MyDAO(ObjectBank bank) {
			this._bank = bank;
		}

		public MyDAO(Repository repo) {

			ObjectBank bank = repo.getComponentContext().getBean(
					XGitContext.component.objects, ObjectBank.class);

			this._bank = bank;

		}

		@Override
		public TreeObject getTree(ObjectId id) {
			InputStream in = null;
			try {
				GitObject obj = _bank.object(id);
				GitObjectEntity ent = obj.entity();
				in = ent.openPlainInput();
				TreeObject tree = new TreeObject();
				TreeBuilder builder = new TreeBuilder(tree);
				builder.parse(in);
				return tree;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOTools.close(in);
			}
		}

		@Override
		public ObjectId save(TreeObject tree) throws IOException {
			TreeWriter tw = new TreeWriter();
			tw.setTree(tree);
			return tw.flush(_bank);
		}

	}

	private static class TreeWriter {

		private byte[] _data;

		public ObjectId flush(ObjectBank bank) throws IOException {
			byte[] ba = this._data;
			GitObjectBuilder builder = bank.newBuilder(GitObject.TYPE.tree,
					ba.length);
			builder.write(ba, 0, ba.length);
			GitObject obj = builder.create();
			return obj.id();
		}

		public void setTree(TreeObject tree)
				throws UnsupportedEncodingException, IOException {

			// make table

			Map<String, ObjectId> table = new HashMap<String, ObjectId>();
			Map<String, TreeItem> items = tree.getItems();
			for (TreeItem item : items.values()) {

				PlainId pid = item.getId();
				String name = item.getName();
				int mode = item.getMode();

				ObjectId id = PlainId.convert(pid);
				boolean is_sha1 = this.is_sha1(id);
				String not_sha1 = is_sha1 ? "" : "L";

				String key = String.format("%h%s %s", mode, not_sha1, name);
				table.put(key, id);

			}

			// write to stream

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String enc = "utf-8";
			List<String> keys = new ArrayList<String>(table.keySet());
			for (String key : keys) {

				ObjectId id = table.get(key);
				boolean is_sha1 = this.is_sha1(id);

				byte[] ba1 = key.getBytes(enc);
				byte[] ba2 = id.toByteArray();

				baos.write(ba1);
				baos.write(0);
				if (!is_sha1) {
					baos.write(ba2.length);
				}
				baos.write(ba2);
			}

			this._data = baos.toByteArray();
		}

		private boolean is_sha1(ObjectId id) {
			return (id.toString().length() == 40);
		}

	}

	private static class InnerTreeItem {

		private String name;
		private boolean has_L;
		private int mode;
		private ObjectId id;

		public TreeItem create() {

			PlainId pid = PlainId.convert(this.id);

			TreeItem item = new TreeItem();
			item.setName(this.name);
			item.setMode(this.mode);
			item.setId(pid);
			return item;
		}

		public void load_text(byte[] ba) throws UnsupportedEncodingException {

			final String enc = "utf-8";
			final String s = new String(ba, enc);

			final int i = s.indexOf(' ');
			if (i < 0) {
				return;
			}

			final String p1 = s.substring(0, i); // '100644L name'
			final String p2 = s.substring(i + 1);

			final String mode;
			final int il = p1.indexOf('L');
			if (il < 0) {
				this.has_L = false;
				mode = p1;
			} else {
				this.has_L = true;
				mode = p1.substring(0, il);
			}
			this.mode = Integer.parseInt(mode, 16);
			this.name = p2;

		}

		public void load_id(byte[] ba) {
			ObjectId id = ObjectId.Factory.create(ba);
			this.id = id;
		}

	}

	private static class TreeBuilder {

		private final TreeObject _tree;

		public TreeBuilder(TreeObject tree) {
			this._tree = tree;
		}

		public void parse(InputStream in) throws IOException {
			in = this.wrap_with_buffered_stream(in);
			Map<String, TreeItem> items = new HashMap<String, TreeItem>();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (;;) {
				InnerTreeItem item = this.read_item(in, baos);
				if (item == null) {
					break;
				} else {
					String key = item.name;
					TreeItem value = item.create();
					items.put(key, value);
				}
			}
			_tree.setItems(items);
		}

		private InnerTreeItem read_item(InputStream in,
				ByteArrayOutputStream baos) throws IOException {

			InnerTreeItem item = new InnerTreeItem();

			// part 1
			final byte[] ba1 = this.read_until_0(in, baos);
			item.load_text(ba1);

			if (item.name == null) {
				return null;
			}

			final int id_size;
			if (item.has_L) {
				id_size = in.read();
			} else {
				id_size = 20; // default id-size: 20 bytes (sha-1)
			}

			// part 2
			final byte[] ba2 = new byte[id_size];
			final int cb = in.read(ba2);
			if (cb == id_size) {
				item.load_id(ba2);
			} else {
				throw new RuntimeException("bad id size: " + cb);
			}

			return item;
		}

		private InputStream wrap_with_buffered_stream(InputStream in) {
			// TODO make a buffered stream
			return in;
		}

		private byte[] read_until_0(InputStream in, ByteArrayOutputStream baos)
				throws IOException {

			baos.reset();
			for (;;) {
				int b = in.read();
				if (b <= 0) {
					break;
				} else {
					baos.write(b);
				}
			}
			return baos.toByteArray();
		}
	}

}
