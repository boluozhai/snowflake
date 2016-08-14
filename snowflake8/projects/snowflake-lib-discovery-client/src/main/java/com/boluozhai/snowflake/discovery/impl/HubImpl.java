package com.boluozhai.snowflake.discovery.impl;

import java.io.IOException;

import com.boluozhai.snowflake.discovery.scheme.PrivateScheme;
import com.boluozhai.snowflake.discovery.scheme.PublicScheme;
import com.boluozhai.snowflake.discovery.udp.Endpoint;
import com.boluozhai.snowflake.discovery.udp.Hub;
import com.boluozhai.snowflake.discovery.udp.HubBuilder;
import com.boluozhai.snowflake.discovery.udp.HubRuntime;
import com.boluozhai.snowflake.discovery.udp.PrivateHandler;
import com.boluozhai.snowflake.discovery.udp.PublicHandler;
import com.boluozhai.snowflake.discovery.utils.DiscoveryUtils;
import com.boluozhai.snowflake.util.IOTools;

final class HubImpl implements Hub {

	private class Data {

		public PublicHandler publicHandler;
		public PrivateHandler privateHandler;
		// public SnowContext context;
		public int port;
	}

	private class Runtime implements HubRuntime {

		private Thread _thread;
		private boolean _started;
		private boolean _stopped;
		private boolean _do_stop;
		private final Data _data;
		private UdpConnection _con;

		public Runtime(Data data) {
			this._data = data;
		}

		@Override
		public void tx(PublicScheme data) throws IOException {
			byte[] ba = DiscoveryUtils.publicScheme2bytes(data);
			String host = data.getTo_host();
			int port = data.getTo_port();
			this._con.send(ba, host, port);
		}

		@Override
		public void tx(PrivateScheme data) throws IOException {
			byte[] ba = DiscoveryUtils.privateScheme2bytes(data);
			String host = data.getTo_host();
			int port = data.getTo_port();
			this._con.send(ba, host, port);
		}

		@Override
		public void run() {
			for (;;) {
				if (this._do_stop) {
					break;
				}
				this.safe_run();
				if (this._do_stop) {
					break;
				} else {
					this.safe_sleep(5000);
				}
			}
		}

		private void safe_sleep(int interval) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void safe_run() {
			UdpConnection con = null;
			try {
				this._started = true;
				con = new UdpConnection(this._data.port);
				con.open();
				this._con = con;
				con.loop();
			} finally {
				this._stopped = true;
				IOTools.close(con);
			}
		}

		@Override
		public void start() {
			Thread thread = this._thread;
			if (thread == null) {
				thread = new Thread(this);
				this._thread = thread;
				thread.start();
			}
		}

		@Override
		public void stop() {
			this._do_stop = true;
			IOTools.close(this._con);
		}

		@Override
		public void join() throws InterruptedException {
			this._thread.join();
		}

		@Override
		public boolean started() {
			return this._started;
		}

		@Override
		public boolean stopped() {
			return this._stopped;
		}

		@Override
		public Hub getHub() {
			return HubImpl.this;
		}

	}

	private Data _data;

	public HubImpl(HubBuilder builder) {

		Data data = new Data();

		data.publicHandler = builder.getPublicHandler();
		data.privateHandler = builder.getPrivateHandler();
		// data.context = builder.getContext();
		data.port = builder.getPort();

		this._data = data;

	}

	@Override
	public PublicHandler getPublicHandler() {
		return this._data.publicHandler;
	}

	@Override
	public PrivateHandler getPrivateHandler() {
		return this._data.privateHandler;
	}

	@Override
	public int getPort() {
		return this._data.port;
	}

	@Override
	public HubRuntime createRuntime() {
		Runtime rt = new Runtime(this._data);
		return rt;
	}

	@Override
	public Endpoint openEndpoint() throws IOException {
		return EndpointImpl.open(this);
	}

}
