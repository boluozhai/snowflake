package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.InflaterOutputStream;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.vfs.FileObject;

final class FileZippedOutputStream extends OutputStream {

	public static class Builder {

		private final FileObjectBankCore _core;
		private FileObject _object;

		public Builder(FileObjectBankCore core) {
			this._core = core;
		}

		public OutputStream open() throws IOException {
			return new FileZippedOutputStream(this);
		}

		public void setObject(FileObject obj) {
			this._object = obj;
		}

	}

	private final VFile _file_temp;
	private final VFile _file_final;
	private final MessageDigest _md;
	private final ObjectId _reg_id;
	private OutputStream _output1;
	private OutputStream _output2;

	public FileZippedOutputStream(Builder init) throws IOException {

		FileObjectBankCore core = init._core;

		this._reg_id = init._object.id();
		this._file_final = init._object.getFile();
		this._file_temp = core.getTemporaryFileManager().newTemporaryFile();
		this._md = core.getHashAlgorithmProvider().getMessageDigest();

		OutputStream out_hash = new HashAdapterOutputStream(_md);

		VFSIO io = VFSIO.Agent.getInstance(core.getContext());
		this._output1 = io.output(_file_temp, true);
		this._output2 = new InflaterOutputStream(out_hash);

	}

	@Override
	public void write(int b) throws IOException {
		_output1.write(b);
		_output2.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		_output1.write(b);
		_output2.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		_output1.write(b, off, len);
		_output2.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		_output1.flush();
		_output2.flush();
	}

	@Override
	public void close() throws IOException {

		ObjectId id = ObjectId.Factory.create(_md.digest());
		ObjectId reg_id = this._reg_id;
		boolean save = id.equals(reg_id);

		// SAVE or DELETE

		VFile file1 = this._file_temp;
		VFile file2 = this._file_final;

		if (save) {

			VFile dir = file2.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}

			boolean rlt = file1.renameTo(file2);
			if (!rlt) {
				file1.delete();
			}
		} else {
			if (file1.exists()) {
				file1.delete();
			}
		}

	}

	private static class HashAdapterOutputStream extends OutputStream {

		private MessageDigest _md;

		public HashAdapterOutputStream(MessageDigest md) {
			this._md = md;
		}

		@Override
		public void write(int b) throws IOException {
			_md.update((byte) b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			_md.update(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			_md.update(b, off, len);
		}

		@Override
		public void flush() throws IOException {
		}

		@Override
		public void close() throws IOException {
			_md = null;
		}

	}

}
