package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.refs.RefPointer;

final class RefPointerImpl implements RefPointer {

	private static class Loader {

		private final Inner inner;

		public Loader(Inner inner) {
			this.inner = inner;
		}

		public Holder load() {
			VFile file = inner.file;
			if (file.exists()) {
				InputStream in = null;
				try {
					in = inner.io.input(file);
					String txt = TextTools.load(in);
					String ref_name = this.parse_ref_name(txt);
					return new Holder(file, ref_name);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOTools.close(in);
				}
			}
			return new Holder(file, null);
		}

		private String parse_ref_name(String txt) {
			final String prefix = "ref:";
			int index;
			if (txt.startsWith(prefix)) {
				index = prefix.length();
			} else {
				throw new RuntimeException("bad ref-name format: " + txt);
			}
			// skip space
			for (;; index++) {
				char ch = txt.charAt(index);
				if (ch == ' ' || ch == '\t') {
					continue;
				} else {
					break;
				}
			}
			final int p0 = index;
			final int len = txt.length();
			final String marks = "\\/-_.";
			for (; index < len; index++) {
				char ch = txt.charAt(index);
				if ('0' <= ch && ch <= '9') {
				} else if ('a' <= ch && ch <= 'z') {
				} else if ('A' <= ch && ch <= 'Z') {
				} else if (marks.indexOf(ch) >= 0) {
				} else {
					break;
				}
			}
			final int p1 = index;
			return txt.substring(p0, p1);
		}

	}

	private static class Saver {

		private final Inner inner;

		public Saver(Inner inner) {
			this.inner = inner;
		}

		public Holder save(String refname) {
			String txt = "ref: " + refname + '\n';
			VFile file = inner.file;
			OutputStream out = null;
			try {
				out = inner.io.output(file, true);
				TextTools.save(txt, out);
				out.close();
				out = null;
				return new Holder(file, refname);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOTools.close(out);
			}
			return new Holder(file, null);
		}
	}

	private static class Holder {

		private final String ref_name;
		private final long last_mod;
		private final VFile file;

		private Holder(VFile _file, String _ref_name) {
			this.file = _file;
			this.ref_name = _ref_name;
			this.last_mod = _file.lastModified();
		}

		public boolean older() {
			return (this.last_mod != file.lastModified());
		}
	}

	private static class Inner {

		private final String name;
		private final VFile file;
		private final VFSIO io;
		private Holder holder;

		public Inner(String name, VPath node) {
			VFile _file = node.file();
			VFSIO _io = VFSIO.Agent.getInstance(_file.vfs().context());
			this.name = name;
			this.file = _file;
			this.io = _io;
		}

		public Holder getHolder(boolean reload) {
			Holder h = this.holder;
			if (h != null) {
				if (h.older() || reload) {
					h = null;
				}
			}
			if (h == null) {
				Loader loader = new Loader(this);
				h = loader.load();
				this.holder = h;
			}
			return h;
		}
	}

	private final Inner inner;

	public RefPointerImpl(String name, VPath node) {
		this.inner = new Inner(name, node);
	}

	@Override
	public String getName() {
		return inner.name;
	}

	@Override
	public boolean exists() {
		return inner.file.exists();
	}

	@Override
	public boolean delete() {
		return inner.file.delete();
	}

	@Override
	public String getRefname() {
		return this.getRefname(false);
	}

	@Override
	public String getRefname(boolean reload) {
		Holder holder = inner.getHolder(reload);
		if (holder == null) {
			return null;
		} else {
			return holder.ref_name;
		}
	}

	@Override
	public void setRefname(String refname) {
		Saver saver = new Saver(inner);
		saver.save(refname);
	}

}
