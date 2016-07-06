package com.boluozhai.snow.discovery;

import java.io.Closeable;
import java.io.IOException;

public interface DiscoveryConnection extends Closeable {

	int getPort();

	void send(DiscoveryPacket packet) throws IOException;

	DiscoveryPacket receive(DiscoveryPacket packet) throws IOException;

	DiscoveryHub getHub();

}
