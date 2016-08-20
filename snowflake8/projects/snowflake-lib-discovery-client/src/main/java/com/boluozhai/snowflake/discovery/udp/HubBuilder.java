package com.boluozhai.snowflake.discovery.udp;

import com.boluozhai.snowflake.context.SnowflakeContext;

public interface HubBuilder {

	Hub create();

	PublicHandler getPublicHandler();

	PrivateHandler getPrivateHandler();

	int getPort();

	SnowflakeContext getContext();

	void setPublicHandler(PublicHandler h);

	void setPrivateHandler(PrivateHandler h);

	void setPort(int port);

	void setContext(SnowflakeContext context);

}
