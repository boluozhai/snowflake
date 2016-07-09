package com.boluozhai.snowflake.test.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;

import com.boluozhai.snow.util.IOTools;

public class TestUsbDriverSpeed implements Runnable {

	public static void main(String[] arg) {
		TestUsbDriverSpeed app = new TestUsbDriverSpeed();
		javax.swing.SwingUtilities.invokeLater(app);
	}

	@Override
	public void run() {

		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(null);
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fc.getSelectedFile();
		InputStream in = null;
		MyOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new MyOutputStream();
			IOTools.pump(in, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.close(in);
			IOTools.close(out);
			System.out.println(out);
		}

	}

	private static class MyOutputStream extends OutputStream {

		private long log_sync;

		private long count;
		private long time0;
		private long time1;

		public MyOutputStream() {
			this.time0 = System.currentTimeMillis();
		}

		@Override
		public void write(int b) throws IOException {
			this.count++;
		}

		@Override
		public void write(byte[] b) throws IOException {
			this.count += b.length;
			this.log();
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			this.count += len;
			this.log();
		}

		private void log() {
			final long dt = count - this.log_sync;
			if (dt > (100 * 1000 * 1000)) {
				log_sync = count;
				System.out.println(count + " done...");
			}
		}

		@Override
		public void close() throws IOException {
			this.time1 = System.currentTimeMillis();
		}

		@Override
		public String toString() {

			final double cost = Math.max(time1 - time0, 1.0) / 1000;
			final double speed = Math.floor(count / cost);

			StringBuilder sb = new StringBuilder();
			sb.append(" count(bytes):").append(this.count);
			sb.append(" cost(s):").append(cost);
			sb.append(" speed(bytes/s):").append((int) speed);
			return sb.toString();

		}

	}

}
