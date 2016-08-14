package com.boluozhai.snowflake.discovery.udp;

import com.boluozhai.snowflake.context.SnowContext;

public interface HubBuilder {

	Hub create();

	PublicHandler getPublicHandler();

	PrivateHandler getPrivateHandler();

	int getPort();

	SnowContext getContext();

	void setPublicHandler(PublicHandler h);

	void setPrivateHandler(PrivateHandler h);

	void setPort(int port);

	void setContext(SnowContext context);

}
