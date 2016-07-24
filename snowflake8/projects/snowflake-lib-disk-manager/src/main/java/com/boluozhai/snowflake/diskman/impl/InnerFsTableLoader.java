package com.boluozhai.snowflake.diskman.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.diskman.model.FsItem;
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

		public FsTable build_fstab() {

			List<Properties> res = this.getResult();

			FsTable fstab = new FsTable();
			fstab.setItems(new HashMap<String, FsItem>());
			fstab.setAlias(new HashMap<String, FsItem>());

			for (Properties pro : res) {
				FsItem item = this.make_item(pro);
				String key = item.getPrimaryKey();
				String[] aliases = item.getAlias();
				fstab.getItems().put(key, item);
				for (String ali : aliases) {
					fstab.getAlias().put(ali, item);
				}
			}

			return fstab;
		}

		private FsItem make_item(Properties pro) {

			Map<String, String> p2 = new HashMap<String, String>();
			for (Object k : pro.keySet()) {
				String key = k.toString();
				String val = pro.getProperty(key);
				p2.put(key, val);
			}

			String pk = p2.get(FsItem.property.device_file);
			String[] alias = this.find_aliases(p2);

			FsItem item = new FsItem();
			item.setProperties(p2);
			item.setPrimaryKey(pk);
			item.setAlias(alias);

			return item;
		}

		private String[] find_aliases(Map<String, String> pro) {

			String prefix = FsItem.property.device_file + ".";
			Set<String> aliases = new HashSet<String>();
			Set<String> keys = pro.keySet();

			for (String key : keys) {
				if (key.startsWith(prefix)) {
					String value = pro.get(key);
					aliases.add(value);
				}
			}

			String pk = pro.get(FsItem.property.device_file);
			aliases.add(pk);

			return aliases.toArray(new String[aliases.size()]);
		}
	}

}
