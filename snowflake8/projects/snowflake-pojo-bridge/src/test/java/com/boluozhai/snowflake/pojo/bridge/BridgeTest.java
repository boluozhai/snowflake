package com.boluozhai.snowflake.pojo.bridge;

import org.junit.Test;

import com.boluozhai.snowflake.pojo.bridge.test.model.Human;
import com.boluozhai.snowflake.pojo.bridge.test.model.Boy;
import com.boluozhai.snowflake.pojo.bridge.test.model.Girl;
import com.boluozhai.snowflake.test.TestBean;
import com.boluozhai.snowflake.test.TestContext;

public class BridgeTest {

	@Test
	public void test() {

		TestBean tb = new TestBean();
		try {
			TestContext context = tb.open(this);
			PojoBridge pb = new PojoBridge(context);
			// BridgeChannel ch = null;

			Human o1 = new Human();
			Boy o2 = new Boy();
			Girl o3 = new Girl();

			pb.from(o1).to(o2).fieldAll().go();

			pb.from(o2).to(o3).fieldAll().go();

		} finally {
			tb.close();
		}

	}

}
