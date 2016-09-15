package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.refs.Ref;

public class InnerRef implements Ref {

	private static class IdSaver {

		private final InnerRef ref;

		public IdSaver(InnerRef self) {
			this.ref = self;
		}

		private IdHolder save(ObjectId id) throws IOException {
			OutputStream out = null;
			try {
				String txt = id.toString();
				VFile file = ref.file;
				out = ref.io.output(file, true);
				TextTools.save(txt, out);
				out.close();
				out = null;
				return new IdHolder(id, file);
			} finally {
				IOTools.close(out);
			}
		}
	}

	private static class IdLoader {

		private final InnerRef ref;

		public IdLoader(InnerRef self) {
			this.ref = self;
		}

		public IdHolder load() throws IOException {
			if (!ref.file.exists()) {
				return new IdHolder(null, ref.file);
			}
			InputStream in = null;
			try {
				in = ref.io.input(ref.file);
				String txt = TextTools.load(in);
				txt = txt.trim();
				ObjectId id = ObjectId.Factory.create(txt);
				return new IdHolder(id, ref.file);
			} finally {
				IOTools.close(in);
			}
		}

	}

	private static class IdHolder {

		private final ObjectId id;
		private final long last_mod;
		private final VFile file;

		private IdHolder(ObjectId _id, VFile _file) {
			this.last_mod = _file.lastModified();
			this.id = _id;
			this.file = _file;
		}

		private boolean older() {
			return (file.lastModified() != this.last_mod);
		}

		public static IdHolder getHolder(InnerRef self) {
			IdHolder h = self.holder;
			if (h != null) {
				if (h.older()) {
					h = null;
				}
			}
			if (h == null) {
				IdLoader loader = new IdLoader(self);
				try {
					h = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return h;
		}

	}

	private final VFile file;
	private final VFSIO io;
	private final String name;
	private IdHolder holder;

	public InnerRef(String name, VPath p) {
		this.file = p.file();
		this.name = name;
		this.io = VFSIO.Agent.getInstance(p.file().vfs().context());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public boolean delete() {
		return file.delete();
	}

	@Override
	public void setId(ObjectId id) {
		try {
			IdSaver saver = new IdSaver(this);
			this.holder = saver.save(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ObjectId getId() {
		return this.getId(false);
	}

	@Override
	public ObjectId getId(boolean reload) {
		if (reload) {
			this.holder = null;
		}
		IdHolder holder = IdHolder.getHolder(this);
		if (holder == null) {
			return null;
		} else {
			return holder.id;
		}
	}

}
