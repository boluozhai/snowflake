package com.boluozhai.snowflake.datatable;

import java.io.File;

import org.junit.Test;

import com.boluozhai.snowflake.datatable.pojo.TestingA;
import com.boluozhai.snowflake.datatable.pojo.TestingB;
import com.boluozhai.snowflake.datatable.pojo.TestingPOJO;
import com.boluozhai.snowflake.test.TestBean;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.util.IOTools;

public class CrudTest {

	@Test
	public void test() {

		DataClient client = null;
		TestBean tb = new TestBean();
		try {
			TestContext context = tb.open(this);

			DataClientConfigurationAPI conf = (DataClientConfigurationAPI) context
					.getAttribute("data-config");
			this.reconfig_data_source(context, conf);
			DataClientFactory factory = conf.configure(context);
			client = factory.open();

			TestingPOJO pp = new TestingPOJO();
			TestingA pa = new TestingA();
			TestingB pb = new TestingB();

			Object[] objs = { pa, pb, pp };

			for (Object obj : objs) {
				this.test_crud(client, obj, "c");
				this.test_crud(client, obj, "cr");
				this.test_crud(client, obj, "cru");
				this.test_crud(client, obj, "crud");

				String[] keys = client.list(obj.getClass());
				System.out.println();
				System.out.print("keys : [");
				for (String k : keys) {
					System.out.print(k + ',');
				}
				System.out.println(']');

			}

		} finally {
			IOTools.close(client);
			tb.close();
		}

	}

	private void test_crud(DataClient client, Object obj, String op) {

		DataLine line = client.line("test-" + op, obj.getClass());

		char[] chs = op.toCharArray();
		for (char ch : chs) {

			switch (ch) {

			case 'c': {
				obj = line.insert(obj);
				break;
			}
			case 'r': {
				obj = line.get(obj.getClass());
				break;
			}
			case 'u': {
				obj = line.update(obj);
				break;
			}
			case 'd': {
				line.delete();
				break;
			}
			default:
				break;
			}

		}

	}

	private void reconfig_data_source(TestContext context,
			DataClientConfigurationAPI conf) {

		File path = context.getWorkingPath();
		path = new File(path, "repo");

		DataClientConfiguration config = (DataClientConfiguration) conf;
		DataSource ds = config.getDataSource();
		ds.setLocation(path.toURI().toString());

	}

}
