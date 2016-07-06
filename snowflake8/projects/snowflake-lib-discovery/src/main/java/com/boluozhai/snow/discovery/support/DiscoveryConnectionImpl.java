package com.boluozhai.snow.discovery.support;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

import com.boluozhai.snow.discovery.DiscoveryConnection;
import com.boluozhai.snow.discovery.DiscoveryHub;
import com.boluozhai.snow.discovery.DiscoveryPacket;
import com.boluozhai.snow.discovery.utils.DiscoveryUtils;

final class DiscoveryConnectionImpl implements DiscoveryConnection {

	private DatagramSocket _socket;
	private final int _port;
	private final DiscoveryHub _hub;

	public DiscoveryConnectionImpl(DiscoveryHub hub, DatagramSocket sock) {
		this._hub = hub;
		this._socket = sock;
		this._port = sock.getLocalPort();
	}

	@Override
	public void close() throws IOException {
		DatagramSocket sock = this._socket;
		this._socket = null;
		if (sock == null) {
			return;
		}
		sock.disconnect();
		sock.close();
	}

	@Override
	public void send(DiscoveryPacket packet) throws IOException {

		Map<String, String> map = DiscoveryUtils.packet2map(packet);
		String str = DiscoveryUtils.map2string(map);
		byte[] buffer = str.getBytes("utf-8");

		DatagramPacket p2 = new DatagramPacket(buffer, buffer.length);
		DatagramSocket sock = this._socket;
		InetAddress addr = sock.getLocalAddress();
		int port = this.getHub().getPort();

		p2.setAddress(addr);
		p2.setPort(port);

		sock.send(p2);

	}

	@Override
	public DiscoveryPacket receive(DiscoveryPacket packet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DiscoveryHub getHub() {
		return this._hub;
	}

	@Override
	public int getPort() {
		return this._port;
	}

}
