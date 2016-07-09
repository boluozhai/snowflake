package com.boluozhai.snow.discovery.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import com.boluozhai.snow.discovery.DiscoveryConnection;
import com.boluozhai.snow.discovery.DiscoveryHub;
import com.boluozhai.snow.discovery.scheme.PrivateScheme;
import com.boluozhai.snow.discovery.scheme.PublicScheme;
import com.boluozhai.snow.discovery.utils.DiscoveryUtils;

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
		private Thread _thread;
		private boolean _do_stop;

		@Override
		public void run() {
			try {
				this.begin();
				this.loop();
			} catch (SocketException e) {
				e.printStackTrace();

				// } catch (IOException e) {
				// e.printStackTrace();

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

			int buffer_size = DiscoveryHubImpl.this.getBufferSize();
			byte[] buffer = new byte[buffer_size];
			DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
			DatagramSocket sock = this._socket;

			OnRxContext rc = new OnRxContext();
			rc.socket = sock;
			rc.packet = pack;

			for (;;) {

				if (_do_stop) {
					break;
				}

				pack.setData(buffer);
				this.receive(rc);

			}

		}

		private void receive(OnRxContext rc) {

			DatagramSocket sock = rc.socket;
			DatagramPacket pack = rc.packet;

			// TODO Auto-generated method stub

			try {

				sock.receive(pack);

				SocketAddress from = pack.getSocketAddress();
				;

				byte[] data = pack.getData();
				int length = pack.getLength();
				int offset = pack.getOffset();

				final boolean isPrivate = PrivateScheme.isPrivateScheme(data,
						offset, length);

				if (isPrivate) {

					PrivateScheme obj = DiscoveryUtils.bytes2privateScheme(
							data, offset, length);
					rc.privateScheme = obj;
					rc.publicScheme = null;
					this.receive_private_scheme(rc, obj);

				} else {

					PublicScheme obj = DiscoveryUtils.bytes2publicScheme(data,
							offset, length);
					rc.privateScheme = null;
					rc.publicScheme = obj;
					this.receive_public_scheme(rc, obj);

				}

			} catch (IOException e) {
				e.printStackTrace();
				this.sleep(500);
			}

		}

		private void receive_private_scheme(OnRxContext rc, PrivateScheme obj) {
			// TODO Auto-generated method stub

		}

		private void receive_public_scheme(OnRxContext rc, PublicScheme obj) {
			// TODO Auto-generated method stub

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

			System.out.println("discovery hub service stopped.");

		}

		public void close() {
			this._do_stop = true;
		}

	}

	@Override
	public void start() {
		Working work = new Working();
		Thread thd = new Thread(work);
		work._thread = thd;
		this._working = work;
		thd.start();
	}

	@Override
	public void stop() {
		this._working.close();
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

	@Override
	public void join() throws InterruptedException {
		this._working._thread.join();
	}

	@Override
	public int getBufferSize() {
		return 1024 * 4;
	}

}
