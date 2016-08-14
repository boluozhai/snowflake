package com.boluozhai.snowflake.discovery.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;
import com.boluozhai.snowflake.discovery.scheme.PublicScheme;
import com.boluozhai.snowflake.discovery.udp.Endpoint;
import com.boluozhai.snowflake.discovery.udp.Hub;
import com.boluozhai.snowflake.discovery.udp.HubRuntime;
import com.boluozhai.snowflake.discovery.udp.PrivateHandler;
import com.boluozhai.snowflake.discovery.utils.DiscoveryUtils;

final class EndpointImpl implements Endpoint {

	private final Hub _hub;
	private DatagramSocket _socket;

	public EndpointImpl(Hub hub) {
		this._hub = hub;
	}

	public static Endpoint open(Hub hub) throws IOException {
		EndpointImpl ep = new EndpointImpl(hub);
		ep.inner_open();
		return ep;
	}

	private void inner_open() throws IOException {
		DatagramSocket ds = new DatagramSocket();
		this._socket = ds;
	}

	@Override
	public synchronized void close() throws IOException {

		DatagramSocket ds = this._socket;
		this._socket = null;

		if (ds != null) {
			ds.disconnect();
			ds.close();
		}

	}

	@Override
	public Hub getHub() {
		return this._hub;
	}

	@Override
	public PrivateScheme request(PrivateScheme data) throws IOException {

		final int size = 1024;
		final byte[] buffer = new byte[size];
		DatagramSocket ds = this._socket;
		DatagramPacket pack = new DatagramPacket(buffer, buffer.length);

		// tx
		pack = this.scheme2pack(data, pack);
		ds.send(pack);

		// rx
		pack.setData(buffer);
		ds.receive(pack);
		return this.pack2scheme(pack);
	}

	private DatagramPacket scheme2pack(PrivateScheme data, DatagramPacket pack)
			throws UnsupportedEncodingException {

		byte[] ba = DiscoveryUtils.privateScheme2bytes(data);
		if (pack == null) {
			pack = new DatagramPacket(ba, ba.length);
		} else {
			pack.setData(ba);
		}

		pack.setAddress(InetAddress.getLoopbackAddress());
		pack.setPort(this._hub.getPort());
		return pack;
	}

	private PrivateScheme pack2scheme(DatagramPacket pack)
			throws UnsupportedEncodingException {

		byte[] data = pack.getData();
		int offset = pack.getOffset();
		int length = pack.getLength();

		return DiscoveryUtils.bytes2privateScheme(data, offset, length);

	}

	@Override
	public void request(PrivateScheme data, PrivateHandler callback) {
		Worker worker = new Worker(data, callback);
		Thread thread = new Thread(worker);
		thread.start();
	}

	private class WorkerBase implements HubRuntime {

		@Override
		public void run() {
		}

		@Override
		public void tx(PublicScheme data) throws IOException {
		}

		@Override
		public void tx(PrivateScheme data) throws IOException {
		}

		@Override
		public void start() {
		}

		@Override
		public void stop() {
		}

		@Override
		public void join() throws InterruptedException {
		}

		@Override
		public boolean started() {
			return false;
		}

		@Override
		public boolean stopped() {
			return false;
		}

		@Override
		public Hub getHub() {
			return null;
		}

	}

	private class Worker extends WorkerBase implements Runnable {

		private PrivateHandler _fn;
		private PrivateScheme _data;

		public Worker(PrivateScheme data, PrivateHandler callback) {
			this._data = data;
			this._fn = callback;
		}

		@Override
		public void run() {

			HubRuntime hr = this;
			PrivateScheme res = null;
			try {
				res = EndpointImpl.this.request(_data);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				_fn.rx(hr, res);
			}

		}

	}

}
