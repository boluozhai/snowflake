package com.boluozhai.snowflake.diskman;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.boluozhai.snowflake.diskman.model.FsItem;
import com.boluozhai.snowflake.diskman.model.FsTable;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;

public class TestDiskMan {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);

			TestContext context = testing.context();
			DiskManager diskman = DiskManager.Agent.getManager(context);
			FsTable fstab = diskman.getFsTable();
			Map<String, FsItem> items = fstab.getItems();
			// Map<String, FsItem> aliases = fstab.getAlias();

			List<String> keys = new ArrayList<String>(items.keySet());
			Collections.sort(keys);

			PrintStream out = System.out;

			for (String key : keys) {
				FsItem item = items.get(key);
				out.println(item.getPrimaryKey());
				this.log_aliases(item);
				this.log_properties(item);
				out.println();
			}

		} finally {
			tester.close(testing);
		}

	}

	private void log_aliases(FsItem item) {
		String[] aliset = item.getAlias();
		Arrays.sort(aliset);
		for (String ali : aliset) {
			System.out.format("    alias: %s\n", ali);
		}
	}

	private void log_properties(FsItem item) {

		Map<String, String> pro = item.getProperties();
		List<String> keys = new ArrayList<String>(pro.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = pro.get(key);
			System.out.format("    %s = %s\n", key, value);
		}

	}
}
