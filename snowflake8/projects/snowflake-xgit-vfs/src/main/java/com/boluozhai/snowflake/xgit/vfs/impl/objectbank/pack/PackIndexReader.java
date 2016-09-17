package com.boluozhai.snowflake.xgit.vfs.impl.objectbank.pack;

import java.io.IOException;

import com.boluozhai.snowflake.vfs.VFile;

public class PackIndexReader {

	private static class Inner {

		private final BinaryReader br;

		public Inner(VFile file) throws IOException {
			this.br = new BinaryReader(file);

			long n0 = br.read_long(4);
			long v2_magic = 0xff744f63L; // { 0xff , 't' , 'O' , 'c' }
			int[] fanout = new int[256];

			if (n0 != v2_magic) {

				// index.v1

				fanout[0] = (int) n0;
				for (int i = 1; i < fanout.length; i++) {
					fanout[i] = br.read_int(4);
				}
				System.out.println("pack-idx.v1");

			} else {
				// index.v2

				int version = br.read_int(4);

				for (int i = 0; i < fanout.length; i++) {
					fanout[i] = br.read_int(4);
				}

				System.out.println("pack-idx.v2");

			}

		}

	}

	private Inner inner;

	public PackIndexReader(VFile file) throws IOException {
		this.inner = new Inner(file);
	}

	public int read() {
		// TODO Auto-generated method stub
		return -1;
	}

}
