package com.boluozhai.snowflake.test.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boluozhai.snow.util.IOTools;

public class DirTools {

	public static void clean(File dir) {
		Runnable task = new DoClean(dir);
		task.run();
	}

	public static void copy(File src, File dest) {
		Runnable task = new DoCopy(src, dest);
		task.run();
	}

	private static class DoCopyPair {

		private final File from;
		private final File to;

		public DoCopyPair(File src, File dest) {
			this.from = src;
			this.to = dest;
		}

		public DoCopyPair child(String name) {
			File p1 = new File(from, name);
			File p2 = new File(to, name);
			return new DoCopyPair(p1, p2);
		}

		public void copy_file_data() throws IOException {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(from);
				out = new FileOutputStream(to);
				IOTools.pump(in, out);
			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}
		}

	}

	private static class DoCopy implements Runnable {

		private final DoCopyPair base;

		public DoCopy(File src, File dest) {
			this.base = new DoCopyPair(src, dest);
		}

		@Override
		public void run() {

			String fmt = "copy files in directory\n    from [%s]\n      to [%s]\n";
			System.out.format(fmt, base.from, base.to);

			try {
				DoCopyPair p = this.base;
				this.copy(p, 50);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}

		}

		private void copy(DoCopyPair p, int depthLimit) throws IOException {

			if (depthLimit < 0) {
				String msg = "the path is too deep: " + p.from;
				throw new RuntimeException(msg);
			}

			final File p1 = p.from;
			final File p2 = p.to;

			if (p1.isDirectory()) {
				this.mkdir(p2);
				String[] list = p1.list();
				for (String item : list) {
					DoCopyPair ch = p.child(item);
					this.copy(ch, depthLimit - 1);
				}
			} else {
				p.copy_file_data();
			}

		}

		private void mkdir(File dir) {
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}

	}

	private static class DoClean implements Runnable {

		private final File target;

		public DoClean(File dir) {
			this.target = dir;
		}

		@Override
		public void run() {

			String fmt = "clean directory\n    at [%s]\n";
			System.out.format(fmt, this.target);

			File path = this.target;
			this.rm_items_in(path, 50);

		}

		private void rm_items_in(File path, int depthLimit) {

			if (depthLimit < 0) {
				String msg = "the path is too deep: " + path;
				throw new RuntimeException(msg);
			}

			if (path.isDirectory()) {
				String[] list = path.list();
				for (String item : list) {
					File ch = new File(path, item);
					this.rm_items_in(ch, depthLimit - 1);
					ch.delete();
				}
			}

		}
	}

}
