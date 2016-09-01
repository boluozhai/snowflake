package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFSContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.refs.Reference;

public class FileReferenceImpl implements Reference {

	private static class CachedData {

		ObjectId id;
		String ref_name;
		long last_modified;

		public void load(VFile file) throws IOException {
			InputStream in = null;
			try {
				if (!file.exists()) {
					return;
				}
				this.last_modified = file.lastModified();
				VFSContext context = file.vfs().context();
				VFSIO io = VFSIO.Agent.getInstance(context);
				in = io.input(file);
				String s = TextTools.load(in).trim();

				if (s.startsWith("ref:")) {
					this.load_ref_name(s);
				} else {
					this.load_id(s);
				}

			} finally {
				IOTools.close(in);
			}
		}

		private void load_ref_name(String s) {
			int index = s.indexOf(':');
			s = s.substring(index + 1).trim();
			this.ref_name = s;
		}

		private void load_id(final String s) {
			final int len = s.length();
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				final char ch = s.charAt(i);
				if ('0' <= ch && ch <= '9') {
					sb.append(ch);
				} else if ('a' <= ch && ch <= 'f') {
					sb.append(ch);
				} else {
					if (sb.length() > 0) {
						break;
					} else {
						continue;
					}
				}
			}
			if (sb.length() > 0) {
				String s2 = sb.toString();
				this.id = ObjectId.Factory.create(s2);
			}
		}

	}

	private final VFile _file;
	private final String _name;
	private CachedData _cached_data;

	public FileReferenceImpl(VFile file, String name) {
		if (file.isDirectory()) {
			String msg = "the file MUST not be a dir: " + file;
			throw new RuntimeException(msg);
		}
		this._file = file;
		this._name = name;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public boolean isId() {
		CachedData cd = this.inner_get_cached_data(false);
		return (cd.id != null);
	}

	@Override
	public boolean isReferenceName() {
		CachedData cd = this.inner_get_cached_data(false);
		return (cd.ref_name != null);
	}

	@Override
	public boolean exists() {
		return this._file.exists();
	}

	private void inner_save_text(String s) {
		OutputStream out = null;
		try {
			VFile file = _file;
			SnowflakeContext context = file.vfs().context();
			VFSIO io = VFSIO.Agent.getInstance(context);
			out = io.output(file, true);
			TextTools.save(s, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOTools.close(out);
		}
	}

	@Override
	public void setTargetId(ObjectId id) {
		String s = id.toString() + "\n";
		this.inner_save_text(s);
	}

	@Override
	public void setTargetReferenceName(String ref_name) {
		StringBuilder sb = new StringBuilder();
		sb.append("ref: ").append(ref_name).append('\n');
		String s = sb.toString();
		this.inner_save_text(s);
	}

	@Override
	public ObjectId getTargetId() {
		CachedData cd = this.inner_get_cached_data(false);
		return cd.id;
	}

	@Override
	public ObjectId loadTargetId() {
		CachedData cd = this.inner_get_cached_data(true);
		return cd.id;
	}

	@Override
	public String getTargetReferenceName() {
		CachedData cd = this.inner_get_cached_data(false);
		return cd.ref_name;
	}

	private CachedData inner_get_cached_data(boolean reload) {
		CachedData cd = this._cached_data;
		if (reload) {
			cd = null;
		}
		if (cd != null) {
			long lm1 = cd.last_modified;
			long lm2 = _file.lastModified();
			if (lm1 != lm2) {
				cd = null;
			}
		}
		if (cd == null) {
			cd = new CachedData();

			try {
				cd.load(_file);
			} catch (IOException e) {
				e.printStackTrace();
			}

			this._cached_data = cd;
		}
		return cd;
	}

}
