package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.DeflaterOutputStream;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;
import com.boluozhai.snowflake.xgit.vfs.FileObject;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.TemporaryFileManager;

final class FileObjectBuilderLarge implements GitObjectBuilder {

	private final String type;
	private final long length;
	private final FileObjectBankCore core;

	private InnerBuilder inner_builder;

	public FileObjectBuilderLarge(FileObjectBankCore _core, String type,
			long length) {
		this.type = type;
		this.length = length;
		this.core = _core;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public long length() {
		return length;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		this.getOutputStream().write(buffer, offset, length);
	}

	@Override
	public OutputStream getOutputStream() {
		return this.get_inner_builder();
	}

	@Override
	public GitObject create() {
		InnerBuilder ib = this.get_inner_builder();
		IOTools.close(ib);
		SnowflakeException err = ib.check();
		if (err == null) {
			ib.save();
		} else {
			ib.delete();
			throw err;
		}
		return ib.get_result_object();
	}

	private class InnerBuilder extends OutputStream {

		private final VFile tmp_file;
		private final FileObjectBank bank;
		private final MessageDigest md;

		private VFile final_file;
		private OutputStream output;
		private GitObject result_object;

		private int head_byte_count;
		private long byte_count;
		private long final_byte_count;

		private InnerBuilder() {

			TemporaryFileManager tmp_file_man = core.getTemporaryFileManager();
			HashAlgorithmProvider hash_alg = core.getHashAlgorithmProvider();

			this.bank = core.getObjectBank();
			this.tmp_file = tmp_file_man.newTemporaryFile();
			this.md = hash_alg.getMessageDigest();

		}

		public void open() throws IOException {

			// make streams

			SnowflakeContext context = core.getContext();
			VFSIO io = VFSIO.Agent.getInstance(context);
			OutputStream fout = io.output(tmp_file, true);
			this.output = new DeflaterOutputStream(fout);

			// write head
			StringBuilder sb = new StringBuilder();
			sb.append(FileObjectBuilderLarge.this.type);
			sb.append(' ');
			sb.append(FileObjectBuilderLarge.this.length);
			sb.append('.');
			String enc = "utf-8";
			byte[] ba = sb.toString().getBytes(enc);
			ba[ba.length - 1] = 0;
			this.write(ba);
			this.head_byte_count = ba.length;

		}

		public GitObject get_result_object() {
			return this.result_object;
		}

		public void delete() {
			this.tmp_file.delete();
		}

		public void save() {

			final VFile file = this.final_file;
			final VFile dir = file.getParentFile();

			if (file.exists()) {
				this.tmp_file.delete();
				return;
			}

			if (!dir.exists()) {
				dir.mkdirs();
			}

			this.tmp_file.renameTo(file);
		}

		public SnowflakeException check() {
			long len1 = this.head_byte_count;
			long len2 = FileObjectBuilderLarge.this.length;
			long len3 = this.final_byte_count;
			if ((len1 + len2) == len3) {
				return null;
			} else {
				return new SnowflakeException("bad length.");
			}
		}

		private final byte[] the_1byte_buffer = new byte[1];

		@Override
		public void write(int b) throws IOException {
			the_1byte_buffer[0] = (byte) b;
			this.write(the_1byte_buffer, 0, 1);
		}

		@Override
		public void write(byte[] b) throws IOException {
			this.write(b, 0, b.length);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			md.update(b, off, len);
			output.write(b, off, len);
			this.byte_count += len;
		}

		@Override
		public void close() throws IOException {

			final OutputStream out = this.output;
			this.output = null;

			if (out != null) {
				out.flush();
				out.close();
				byte[] hash = md.digest();
				ObjectId id = ObjectId.Factory.create(hash);
				FileObject obj = this.bank.object(id);
				this.result_object = obj;
				this.final_byte_count = this.byte_count;
				this.final_file = obj.getFile();
			}

		}
	}

	private InnerBuilder get_inner_builder() {
		try {
			InnerBuilder ib = this.inner_builder;
			if (ib == null) {
				ib = new InnerBuilder();
				ib.open();
				this.inner_builder = ib;
			}
			return ib;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// NOP
		}
	}

}
