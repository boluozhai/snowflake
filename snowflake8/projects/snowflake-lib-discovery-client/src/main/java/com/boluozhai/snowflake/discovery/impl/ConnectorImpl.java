package com.boluozhai.snowflake.discovery.impl;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.discovery.support.DefaultHubHandler;
import com.boluozhai.snowflake.discovery.udp.Hub;
import com.boluozhai.snowflake.discovery.udp.HubBuilder;
import com.boluozhai.snowflake.discovery.udp.HubBuilderFactory;
import com.boluozhai.snowflake.discovery.udp.PrivateHandler;
import com.boluozhai.snowflake.discovery.udp.PublicHandler;
import com.boluozhai.snowflake.test.TestContext;

public class ConnectorImpl implements HubBuilderFactory {

	@Override
	public HubBuilder newBuilder(TestContext context) {
		return new Builder(context);
	}

	private class Builder implements HubBuilder {

		private PublicHandler publicHandler;
		private PrivateHandler privateHandler;
		private int port = 10217;
		private SnowContext context;

		private DefaultHubHandler cached_handler;

		public Builder(TestContext context) {
			this.context = context;
		}

		@Override
		public Hub create() {

			if (this.publicHandler == null) {
				this.publicHandler = this.get_default_handler();
			}

			if (this.privateHandler == null) {
				this.privateHandler = this.get_default_handler();
			}

			return new HubImpl(this);
		}

		private DefaultHubHandler get_default_handler() {
			DefaultHubHandler h = this.cached_handler;
			if (h == null) {
				h = new DefaultHubHandler();
				this.cached_handler = h;
			}
			return h;
		}

		public SnowContext getContext() {
			return context;
		}

		public void setContext(SnowContext context) {
			this.context = context;
		}

		public PublicHandler getPublicHandler() {
			return publicHandler;
		}

		public void setPublicHandler(PublicHandler publicHandler) {
			this.publicHandler = publicHandler;
		}

		public PrivateHandler getPrivateHandler() {
			return privateHandler;
		}

		public void setPrivateHandler(PrivateHandler privateHandler) {
			this.privateHandler = privateHandler;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}

}
