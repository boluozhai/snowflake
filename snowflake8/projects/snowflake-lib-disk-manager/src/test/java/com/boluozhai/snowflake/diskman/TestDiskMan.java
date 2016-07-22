package com.boluozhai.snowflake.diskman;

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

			for (String key : items.keySet()) {
				System.out.println(key);
			}

		} finally {

			tester.close(testing);

		}

	}

}
