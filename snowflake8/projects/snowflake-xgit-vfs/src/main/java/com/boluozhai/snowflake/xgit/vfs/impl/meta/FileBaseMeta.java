package com.boluozhai.snowflake.xgit.vfs.impl.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.meta.BaseMeta;
import com.google.gson.Gson;

public class FileBaseMeta implements BaseMeta {

	public static class BaseBuilder {

		public Class<?> type;
		public String key;
		public VFile file;
		public VFSIO io;

	}

	private final Class<?> _type;
	private final String _key;
	private final VFile _file;
	private final VFSIO _io;
	private final Gson _gs;

	private Object _pojo;
	private String _text;

	public FileBaseMeta(BaseBuilder builder) {
		this._type = builder.type;
		this._file = builder.file;
		this._key = builder.key;
		this._io = builder.io;
		this._gs = new Gson();
	}

	@Override
	public Class<?> getType() {
		return this._type;
	}

	@Override
	public String getKeyName() {
		return this._key;
	}

	@Override
	public String getText() {
		String txt = this._text;
		if (txt == null) {
			try {
				txt = this.loadText();
				this._text = txt;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return txt;
	}

	@Override
	public String loadText() throws IOException {
		InputStream in = null;
		try {
			final VFile file = _file;
			if (file.exists()) {
				in = _io.input(file);
				return TextTools.load(in);
			} else {
				return null;
			}
		} finally {
			IOTools.close(in);
		}
	}

	@Override
	public void saveText(String text) throws IOException {
		OutputStream out = null;
		try {
			out = _io.output(_file, true);
			TextTools.save(text, out);
		} finally {
			IOTools.close(out);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getJSON(Class<T> type) {
		Object pojo = this._pojo;
		if (pojo == null) {
			try {
				pojo = this.loadJSON(type);
				this._pojo = pojo;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (T) pojo;
	}

	@Override
	public <T> T loadJSON(Class<T> type) throws IOException {
		String str = this.loadText();
		return _gs.fromJson(str, type);
	}

	@Override
	public void saveJSON(Object pojo) throws IOException {
		String str = _gs.toJson(pojo);
		this.saveText(str);
	}

}
