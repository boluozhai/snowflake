package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.DeflaterOutputStream;

import com.boluozhai.snow.util.IOTools;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFSTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.vfs.FileObject;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.TemporaryFileManager;

final class FileObjectBuilderSmall implements GitObjectBuilder {

	private final String _type;
	private final long _length;
	private MyOutput _out;
	private final FileObjectBankCore _core;

	public FileObjectBuilderSmall(FileObjectBankCore core, String type,
			long length) {
		this._core = core;
		this._type = type;
		this._length = length;
	}

	@Override
	public String type() {
		return _type;
	}

	@Override
	public long length() {
		return _length;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		this.getOutputStream().write(buffer, offset, length);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		MyOutput out = this._out;
		if (out == null) {
			out = new MyOutput(this._core);
			out.init(this._type, this._length);
			this._out = out;
		}
		return out;
	}

	@Override
	public GitObject create() throws IOException {
		MyOutput out = this._out;
		this._out = null;
		return out.build();
	}

	private static class MyOutput extends OutputStream {

		private final FileObjectBank _bank;
		// private final HashAlgorithmProvider _hash_provider;
		private final TemporaryFileManager _tmp_file_man;
		private final ByteArrayOutputStream _baos;
		private final MessageDigest _md;

		private int _head_size;
		private long _body_size;

		public MyOutput(FileObjectBankCore core) {
			this._baos = new ByteArrayOutputStream();

			this._bank = core.getObjectBank();
			// this._hash_provider = core.getHashAlgorithmProvider();
			this._tmp_file_man = core.getTemporaryFileManager();
			this._md = core.getHashAlgorithmProvider().getMessageDigest();
		}

		public FileObject build() throws IOException {

			final byte[] ba = _baos.toByteArray();
			if (ba.length != (_head_size + _body_size)) {
				throw new RuntimeException("bad size of object.");
			}
			final ObjectId id = ObjectId.Factory.create(_md.digest());

			ComponentContext context = _bank.getComponentContext();
			FileObject obj = _bank.object(id);
			if (obj.exists()) {
				return obj;
			}

			VFile file1 = this._tmp_file_man.newTemporaryFile(null,
					".obj-builder");
			VFile file2 = obj.getFile();

			OutputStream out_p = null; // plain
			OutputStream out_z = null;// zipped
			try {

				VFSIO io = VFSIO.Agent.getInstance(context);
				out_z = io.output(file1, true);
				out_p = new DeflaterOutputStream(out_z);

				out_p.write(ba);

				out_p.flush();
				out_p.close();
				out_p = null;

				out_z.flush();
				out_z.close();
				out_z = null;

				VFSTools.mkdirs4file(file2);
				file1.renameTo(file2);

			} finally {
				IOTools.close(out_p);
				IOTools.close(out_z);
				if (file1.exists()) {
					file1.delete();
				}
			}

			return obj;
		}

		@Override
		public void write(int b) throws IOException {
			_baos.write(b);
			_md.update((byte) b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			_baos.write(b);
			_md.update(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			_baos.write(b, off, len);
			_md.update(b, off, len);
		}

		public void init(String type, long length) throws IOException {

			StringBuilder sb = new StringBuilder();
			sb.append(type);
			sb.append(' ');
			sb.append(length);
			sb.append('\0');
			byte[] ba = sb.toString().getBytes("utf-8");
			this.write(ba);
			this._head_size = ba.length;
			this._body_size = length;

		}
	}

}
