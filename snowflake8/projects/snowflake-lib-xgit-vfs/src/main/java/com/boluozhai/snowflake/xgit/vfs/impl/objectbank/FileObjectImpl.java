package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;

import com.boluozhai.snow.util.IOTools;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.vfs.FileObject;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;

public class FileObjectImpl implements FileObject {

	public static class Builder {

		public ObjectId id;
		public VFile file;
		public FileObjectBank bank;
		public FileObjectBankCore core;

		public FileObject create() {
			return new FileObjectImpl(this);
		}

	}

	private final VFile _file;
	private final ObjectId _id;
	private final FileObjectBank _bank;
	private final FileObjectBankCore _core;

	private ObjectHead _header;

	private FileObjectImpl(Builder builder) {
		this._id = builder.id;
		this._bank = builder.bank;
		this._file = builder.file;
		this._core = builder.core;
	}

	@Override
	public ObjectBank owner() {
		return this._bank;
	}

	@Override
	public ObjectId id() {
		return this._id;
	}

	@Override
	public String type() {
		return this.inner_get_meta().type;
	}

	@Override
	public long length() {
		return this.inner_get_meta().body_size;
	}

	@Override
	public long zippedSize() {
		return this._file.length();
	}

	@Override
	public GitObjectEntity entity() {
		return new MyEntity(this);
	}

	@Override
	public boolean exists() {
		return this._file.exists();
	}

	@Override
	public VFile getFile() {
		return this._file;
	}

	private ObjectHead inner_get_meta() {
		ObjectHead hdr = this._header;
		if (hdr == null) {
			InputStream in = null;
			try {
				in = this.entity().openPlainInput();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOTools.close(in);
			}
			hdr = this._header;
			if (hdr == null) {
				throw new RuntimeException("load a null object-head.");
			}
		}
		return hdr;
	}

	private class MyEntity implements GitObjectEntity {

		private final FileObjectBankCore _core;
		private final FileObjectImpl _obj;

		public MyEntity(FileObjectImpl obj) {
			this._core = obj._core;
			this._obj = obj;
		}

		@Override
		public InputStream openPlainInput() throws IOException {
			InputStream zipped = this.openZippedInput();
			InputStream plain = new InflaterInputStream(zipped);
			ObjectHeadLoader ohl = new ObjectHeadLoader();
			ObjectHead hdr = ohl.load(plain);
			this._obj._header = hdr;
			return plain;
		}

		@Override
		public InputStream openZippedInput() throws IOException {
			FileObjectImpl obj = this._obj;
			ComponentContext context = obj._bank.getComponentContext();
			VFSIO io = VFSIO.Agent.getInstance(context);
			return io.input(obj._file);
		}

		@Override
		public OutputStream openZippedOutput() throws IOException {
			VFile file = this._obj._file;
			if (file.exists()) {
				return new NopOutputStream();
			} else {
				FileZippedOutputStream.Builder builder;
				builder = new FileZippedOutputStream.Builder(this._core);
				builder.setObject(this._obj);
				return builder.open();
			}
		}
	}

	@Override
	public void delete() {
		this._file.delete();
	}

}
