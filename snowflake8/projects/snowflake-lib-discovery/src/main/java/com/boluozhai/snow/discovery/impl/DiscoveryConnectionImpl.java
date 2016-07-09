package com.boluozhai.snow.discovery.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.boluozhai.snow.discovery.DiscoveryConnection;
import com.boluozhai.snow.discovery.DiscoveryHub;
import com.boluozhai.snow.discovery.scheme.PrivateScheme;
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
	public void send(PrivateScheme packet) throws IOException {

		packet.setTimestamp(System.currentTimeMillis());
		byte[] data = DiscoveryUtils.privateScheme2bytes(packet);

		DatagramPacket udp = new DatagramPacket(data, data.length);
		DatagramSocket sock = this._socket;
		InetAddress addr = sock.getLocalAddress();
		int port = this.getHub().getPort();

		udp.setAddress(addr);
		udp.setPort(port);
		udp.setData(data);

		sock.send(udp);

	}

	@Override
	public PrivateScheme receive(PrivateScheme packet) throws IOException {

		int size = this.getHub().getBufferSize();
		byte[] buffer = new byte[size];
		DatagramPacket udp = new DatagramPacket(buffer, buffer.length);
		this._socket.receive(udp);

		int offset = udp.getOffset();
		int length = udp.getLength();
		packet = DiscoveryUtils.bytes2privateScheme(buffer, offset, length);

		return packet;
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
