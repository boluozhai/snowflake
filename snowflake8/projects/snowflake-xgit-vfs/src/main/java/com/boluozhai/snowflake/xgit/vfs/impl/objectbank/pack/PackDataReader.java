package com.boluozhai.snowflake.xgit.vfs.impl.objectbank.pack;

import java.io.IOException;

import com.boluozhai.snowflake.vfs.VFile;

public class PackDataReader {

	private static interface Inner {

		int readEntity() throws IOException;
	}

	private static class Inner2 implements Inner {

		private final BinaryReader br;

		public Inner2(BinaryReader aBR) {
			this.br = aBR;
		}

		@Override
		public int readEntity() throws IOException {

			return br.read_type_and_langth();

		}

	}

	private Inner inner;

	public PackDataReader(VFile file) throws IOException {

		BinaryReader br = new BinaryReader(file);
		String signature = br.read_string(4);
		int version = br.read_int(4);
		int count_objs = br.read_int(4);

		System.out.format("{'%s', version:%d, cnt_objs:%d}\n", signature,
				version, count_objs);

		this.inner = new Inner2(br);

	}

	public int read() throws IOException {

		return inner.readEntity();

	}

}
