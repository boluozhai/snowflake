package com.boluozhai.snowflake.xgit.vfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.zip.InflaterOutputStream;

import org.junit.Test;

import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class TestObjectBank {

	class InnerContext {

		public FileRepository repository;
		public FileWorkspace workspace;
		public FileObjectBank bank;

	}

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context_t = testing.context();

			File wkdir = context_t.getWorkingPath();
			URI uri = (new File(wkdir, "repository")).toURI();

			RepositoryManager repo_man = XGit.getRepositoryManager(context_t);
			FileRepository repo = (FileRepository) repo_man.open(context_t,
					uri, null);
			ComponentContext context_repo = repo.getComponentContext();
			FileObjectBank bank = context_repo.getBean(
					XGitContext.component.objects, FileObjectBank.class);
			FileWorkspace works = context_repo.getBean(
					XGitContext.component.working, FileWorkspace.class);

			InnerContext ic = new InnerContext();
			ic.bank = (FileObjectBank) bank;
			ic.repository = repo;
			ic.workspace = works;

			this.test_zipped_read(ic);
			this.test_zipped_write(ic);
			this.test_plain_read(ic);

			this.test_new_builder_2(ic);

		} catch (Exception e) {
			throw new RuntimeException(e);

		} finally {
			tester.close(testing);
		}

	}

	private void test_plain_read(InnerContext ic) throws IOException {

		String idstr = "d959e0199f6ec91bb9fa20c95e63b8ada8c7b531";
		InputStream in = null;
		OutputStream out = null;

		try {

			System.out.format("### read a git-object in plain mode ###\n");

			ObjectId id = ObjectId.Factory.create(idstr);
			ObjectBank bank = ic.bank;
			GitObject obj = bank.object(id);
			GitObjectEntity entity = obj.entity();

			in = entity.openPlainInput();
			out = System.out;

			long length = obj.length();
			String type = obj.type();

			System.out.format("    id: %s\n", id);
			System.out.format("  type: %s\n", type);
			System.out.format("  size: %d\n", length);

			IOTools.pump(in, out);
			out = null;

		} finally {

			IOTools.close(in);
			// IOTools.close(out);

			System.out.println();

		}

	}

	private void test_zipped_write(InnerContext ic) throws IOException {

		String idstr = "d998dce34e394d9e45523a55671a2fe7827b9d67";
		InputStream in = null;
		OutputStream out = null;

		try {

			System.out.format("### write a git-object in zipped mode ###\n");

			ObjectId id = ObjectId.Factory.create(idstr);
			ObjectBank bank = ic.bank;
			GitObject obj = bank.object(id);
			GitObjectEntity entity = obj.entity();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// read zipped
			in = entity.openZippedInput();
			out = baos;
			IOTools.pump(in, out);
			IOTools.close(in);
			IOTools.close(out);
			in = null;
			out = null;

			// while exists
			in = new ByteArrayInputStream(baos.toByteArray());
			out = entity.openZippedOutput();
			IOTools.pump(in, out);
			IOTools.close(in);
			IOTools.close(out);
			in = null;
			out = null;

			// while not exists
			obj.delete();
			in = new ByteArrayInputStream(baos.toByteArray());
			out = entity.openZippedOutput();
			IOTools.pump(in, out);
			IOTools.close(in);
			IOTools.close(out);
			in = null;
			out = null;

		} finally {
			IOTools.close(in);
			IOTools.close(out);
			System.out.println();
		}

	}

	private void test_zipped_read(InnerContext ic) throws IOException {

		String idstr = "d959e0199f6ec91bb9fa20c95e63b8ada8c7b531";
		InputStream in = null;
		OutputStream out = null;

		try {

			System.out.format("### read a git-object in zipped mode ###\n");

			CheckSumOutput check = new CheckSumOutput("sha-1");

			ObjectId id = ObjectId.Factory.create(idstr);
			ObjectBank bank = ic.bank;
			GitObject obj = bank.object(id);
			GitObjectEntity entity = obj.entity();

			in = entity.openZippedInput();
			out = new InflaterOutputStream(check);

			IOTools.pump(in, out);
			IOTools.close(out);
			out = null;

			System.out.format("        id: %s\n", id);
			System.out.format("    result: %s\n", check);

		} finally {

			IOTools.close(in);
			IOTools.close(out);

			System.out.println();

		}

	}

	private void test_new_builder_2(InnerContext ic) throws IOException {
		test_new_builder_x(ic, 2);
	}

	private void test_new_builder_x(InnerContext ic, int x) throws IOException {

		InputStream in = null;
		OutputStream out = null;

		try {

			System.out.format("test object builder(x=%d)\n", x);
			String filename = "file_4_object_builder_" + x + ".txt";

			FileObjectBank bank = ic.bank;
			FileWorkspace works = ic.workspace;

			VFSIO io = VFSIO.Agent.getInstance(ic.repository.context());
			VFile file = works.getFile().child(filename);

			GitObjectBuilder builder = bank.newBuilder("blob", file.length());

			out = builder.getOutputStream();
			in = io.input(file);

			IOTools.pump(in, out);

			GitObject obj = builder.create();
			ObjectId id = obj.id();
			System.out.println("  id=" + id);

		} finally {

			IOTools.close(in);
			IOTools.close(out);

		}

	}

	private static interface MetaKey {

		String type = "type";
		String head_size = "head_size";
		String body_size = "body_size";
		String hash = "hash";
		String zipped_size = "zipped_size";

	}

	private static class MetaData {

		interface keys extends MetaKey {
		}

		private Map<String, String> table;

		public void put(String k, String v) {
			// TODO Auto-generated method stub

		}

		public void put(String k, int v) {
			// TODO Auto-generated method stub

		}

		public void put(String k, long v) {
			// TODO Auto-generated method stub

		}

		public String getString(String hash) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class ObjectHeader {

		private MetaData real = new MetaData();
		private MetaData defined = new MetaData();

		public void load(ByteArrayOutputStream baos)
				throws UnsupportedEncodingException {

			String s = baos.toString("utf-8");
			int index = s.indexOf(' ');
			if (index < 0) {
				throw new RuntimeException("bad format");
			}
			String s1 = s.substring(0, index);
			String s2 = s.substring(index + 1);

			real.put(MetaKey.type, s1);
			defined.put(MetaKey.type, s1);
			defined.put(MetaKey.body_size, s2);

		}

	}

	private static class CheckSumOutput extends OutputStream {

		private MessageDigest _md;
		private ObjectHeader _header;
		private int _header_size;
		private long _length;
		private ByteArrayOutputStream _header_buffer;

		// accept plain raw object data

		public CheckSumOutput(String alg) {
			if (alg == null) {
				alg = "sha-1";
			}
			try {
				MessageDigest md = MessageDigest.getInstance(alg);
				this._md = md;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			this._header_buffer = new ByteArrayOutputStream();
		}

		private void parse_header(int b) throws UnsupportedEncodingException {
			if (_header == null) {
				if (b == 0) {
					// head end
					ObjectHeader hdr = new ObjectHeader();
					hdr.load(this._header_buffer);
					this._header_size = this._header_buffer.size();
					this._header = hdr;
					this._header_buffer = null;
				} else {
					this._header_buffer.write(b);
				}
			}
		}

		@Override
		public void write(int b) throws IOException {
			_md.update((byte) b);
			_length++;
			if (_header == null) {
				this.parse_header(b);
			}
		}

		@Override
		public void write(byte[] b) throws IOException {
			_md.update(b);
			_length += b.length;
			if (_header == null) {
				for (byte b2 : b) {
					this.parse_header(b2);
				}
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			_md.update(b, off, len);
			_length += len;
			if (_header == null) {
				for (byte b2 : b) {
					this.parse_header(b2);
				}
			}
		}

		@Override
		public void close() throws IOException {

			try {

				byte[] hash = _md.digest();
				ObjectId id = ObjectId.Factory.create(hash);
				this._header.real.put(MetaKey.hash, id.toString());

				long sz_head = this._header_size + 1;
				long sz_body = this._length - sz_head;

				this._header.real.put(MetaKey.head_size, sz_head);
				this._header.real.put(MetaKey.body_size, sz_body);

				this._md = null;
				this._header_buffer = null;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public String toString() {

			ObjectHeader hdr = this._header;

			String id = hdr.real.getString(MetaKey.hash);
			String type = hdr.real.getString(MetaKey.type);
			String size = hdr.real.getString(MetaKey.body_size);

			String fmt = "%s type:%s length:%s";
			return String.format(fmt, id, type, size);

		}

	}

}
