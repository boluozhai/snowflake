package com.boluozhai.snowflake.discovery.udp;

import java.io.IOException;

import com.boluozhai.snowflake.discovery.scheme.PublicScheme;

public interface PublicDispatcher {

	void tx(PublicScheme data) throws IOException;

}
