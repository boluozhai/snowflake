package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snowflake.vfs.VFile;
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

		public FileObject create() {
			return new FileObjectImpl(this);
		}

	}

	private final VFile _file;
	private final ObjectId _id;
	private final FileObjectBank _bank;

	private FileObjectImpl(Builder builder) {
		this._id = builder.id;
		this._bank = builder.bank;
		this._file = builder.file;
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
		return this.inner_get_meta().length;
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

	private MyMeta inner_get_meta() {
		return null;
	}

	private class MyMeta {

		long length;
		String type;

	}

	private class MyEntity implements GitObjectEntity {

		public MyEntity(FileObjectImpl fileObjectImpl) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public InputStream openPlainInput() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream openZippedInput() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public OutputStream openZippedOutput() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
