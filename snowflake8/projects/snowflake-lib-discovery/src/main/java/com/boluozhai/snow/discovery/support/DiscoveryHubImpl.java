package com.boluozhai.snow.discovery.support;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import com.boluozhai.snow.discovery.DiscoveryConnection;
import com.boluozhai.snow.discovery.DiscoveryHub;

final class DiscoveryHubImpl implements DiscoveryHub {

	private final int _port;
	private Working _working;

	public DiscoveryHubImpl(int port) {
		this._port = port;
	}

	public static DiscoveryHub open(int port) {
		return new DiscoveryHubImpl(port);
	}

	private class Working implements Runnable {

		private DatagramSocket _socket;

		@Override
		public void run() {
			try {
				this.begin();
				this.loop();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				this.end();
			}
		}

		private void begin() throws SocketException {

			final int port = DiscoveryHubImpl.this._port;

			DatagramSocket ds = new DatagramSocket(port);
			this._socket = ds;

			System.out.println("listen UDP at " + port);

		}

		private void loop() {

			byte[] buffer = new byte[1024 * 8];
			DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
			DatagramSocket sock = this._socket;

			for (;;) {

				pack.setData(buffer);
				this.receive(sock, pack);

			}

		}

		private void receive(DatagramSocket sock, DatagramPacket pack) {
			// TODO Auto-generated method stub

			try {
				sock.receive(pack);

				SocketAddress from = pack.getSocketAddress();
				int len = pack.getLength();
				int off = pack.getOffset();

				System.out.format("rx %d bytes from %s", len, from);

			} catch (IOException e) {
				e.printStackTrace();
				this.sleep(500);
			}

		}

		private void sleep(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void end() {

			DatagramSocket sock = this._socket;
			this._socket = null;

			if (sock == null) {
				return;
			}

			sock.disconnect();
			sock.close();

		}

	}

	@Override
	public void start() {
		Working work = new Working();
		this._working = work;
		Thread thd = new Thread(work);
		thd.start();
	}

	@Override
	public void stop() {
		this._working = null;
	}

	@Override
	public DiscoveryConnection connect() throws IOException {
		DatagramSocket ds = new DatagramSocket();
		return new DiscoveryConnectionImpl(this, ds);
	}

	@Override
	public int getPort() {
		return this._port;
	}

}
