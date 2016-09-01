package com.boluozhai.snowflake.discovery.udp;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;

public interface Endpoint extends Closeable {

	Hub getHub();

	PrivateScheme request(PrivateScheme data) throws IOException;

	void request(PrivateScheme data, PrivateHandler callback);

}
