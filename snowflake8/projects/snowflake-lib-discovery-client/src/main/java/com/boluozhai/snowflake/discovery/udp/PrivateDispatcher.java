package com.boluozhai.snowflake.discovery.udp;

import java.io.IOException;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;

public interface PrivateDispatcher {

	void tx(PrivateScheme data) throws IOException;

}
