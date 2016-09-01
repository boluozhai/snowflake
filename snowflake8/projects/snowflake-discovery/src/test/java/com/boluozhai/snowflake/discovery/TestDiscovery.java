package com.boluozhai.snowflake.discovery;

import java.io.IOException;

import org.junit.Test;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;
import com.boluozhai.snowflake.discovery.udp.Endpoint;
import com.boluozhai.snowflake.discovery.udp.Hub;
import com.boluozhai.snowflake.discovery.udp.HubBuilder;
import com.boluozhai.snowflake.discovery.udp.HubBuilderFactory;
import com.boluozhai.snowflake.discovery.udp.HubRuntime;
import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.util.IOTools;

public class TestDiscovery {

	// @Test
	
	public void test() {

		Tester tester = null;
		Testing testing = null;
		Endpoint ep = null;

		try {

			tester = Tester.Factory.newInstance();
			testing = tester.open(this);

			TestContext context = testing.context();

			HubBuilderFactory factory = Hub.Factory.getFactory(context);
			HubBuilder builder = factory.newBuilder(context);
			Hub hub = builder.create();
			HubRuntime runtime = hub.createRuntime();
			ep = hub.openEndpoint();

			runtime.start();

			PrivateScheme request = new PrivateScheme();

			PrivateScheme response = ep.request(request);

			this.log("req:", request);
			this.log("res:", response);

			runtime.stop();
			runtime.join();

		} catch (InterruptedException e) {

			throw new RuntimeException(e);

		} catch (IOException e) {

			throw new RuntimeException(e);

		} finally {
			tester.close(testing);
			IOTools.close(ep);
		}

	}

	private void log(String string, PrivateScheme request) {
		// TODO Auto-generated method stub

	}

}
