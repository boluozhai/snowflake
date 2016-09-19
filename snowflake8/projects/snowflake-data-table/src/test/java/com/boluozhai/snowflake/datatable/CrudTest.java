package com.boluozhai.snowflake.datatable;

import org.junit.Test;

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
			DataClientFactory factory = conf.configure(context);
			client = factory.open();

			TestingPOJO pojo = new TestingPOJO();

			// C
			Transaction tran = client.beginTransaction();
			pojo = client.insert(pojo);
			tran.commit();

			// R
			String id = pojo.getId();
			pojo = client.get(id, TestingPOJO.class);

			// U
			tran.begin();
			pojo.setSomeData(System.currentTimeMillis() + "");
			pojo = client.update(pojo);
			tran.commit();

			// D
			tran.begin();
			client.delete(id);
			tran.commit();

		} finally {
			IOTools.close(client);
			tb.close();
		}

	}

}
