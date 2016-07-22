package com.boluozhai.snowflake.diskman.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.diskman.model.FsTable;
import com.boluozhai.snowflake.runtime.ErrorHandler;
import com.boluozhai.snowflake.runtime.RuntimeExe;
import com.boluozhai.snowflake.runtime.SubProcess;
import com.boluozhai.snowflake.runtime.SubProcessBuilder;

final class InnerFsTableLoader {

	private final SnowContext _context;

	public InnerFsTableLoader(SnowContext context) {
		this._context = context;
	}

	public FsTable load() {

		try {

			String cmd0 = "udisks --dump";
			String[] cmd = { cmd0, cmd0, cmd0 };

			MyErrorHandler hError = new MyErrorHandler();
			MyOutputHandler hOut = new MyOutputHandler();

			RuntimeExe rt = RuntimeExe.Agent.getInstance(_context);
			SubProcessBuilder builder = rt.newSubProcessBuilder();

			builder.setCommand(cmd);
			builder.setErrorHandler(hError);
			builder.setOutputHandler(hOut);

			SubProcess proc = builder.create();
			proc.start();
			proc.join();

			return hOut.build_fstab();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);

		} finally {
		}

	}

	private class MyErrorHandler implements ErrorHandler {

		@Override
		public void onLine(SubProcess sp, String text) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(SubProcess sp, Throwable e) {
			// TODO Auto-generated method stub

		}
	}

	private class MyOutputHandler extends FstabBuilder {

		private int line_number;

		@Override
		public void onLine(SubProcess sp, String text) {
			// TODO Auto-generated method stub

			int ln = this.line_number++;
			System.out.format("[%4d]%s\n", ln, text);

			super.onLine(sp, text);

		}

		public FsTable build_fstab() {
			// TODO Auto-generated method stub

			List<Properties> res = this.getResult();

			int index = 0;
			for (Properties pro : res) {

				System.out.format("p%d\n", index++);

				ArrayList<String> keys = new ArrayList<String>();
				for (Enumeration<Object> k = pro.keys(); k.hasMoreElements();) {
					String key = k.nextElement().toString();
					keys.add(key);
				}
				Collections.sort(keys);

				for (String key : keys) {
					String value = pro.getProperty(key);
					System.out.format("  %s = %s\n", key, value);
				}

			}

			return null;
		}
	}

}
