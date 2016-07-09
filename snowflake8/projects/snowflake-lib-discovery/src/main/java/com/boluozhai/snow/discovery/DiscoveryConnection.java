package com.boluozhai.snow.discovery;

import java.io.Closeable;
import java.io.IOException;

import com.boluozhai.snow.discovery.scheme.PrivateScheme;

public interface DiscoveryConnection extends Closeable {

	int getPort();

	void send(PrivateScheme packet) throws IOException;

	PrivateScheme receive(PrivateScheme packet) throws IOException;

	DiscoveryHub getHub();

}
