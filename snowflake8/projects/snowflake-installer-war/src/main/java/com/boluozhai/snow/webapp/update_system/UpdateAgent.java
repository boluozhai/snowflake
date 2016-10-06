package com.boluozhai.snow.webapp.update_system;

import java.io.IOException;

import com.boluozhai.snow.webapp.update_system.pojo.WebResponse;

public class UpdateAgent {

	private final UpdateClient _client;

	public UpdateAgent(UpdateClient client) {
		this._client = client;
	}

	public WebResponse check() throws IOException {
		_client.check();
		return this.response();
	}

	public WebResponse download() throws IOException {
		_client.check();
		_client.download();
		return this.response();
	}

	public WebResponse clean() {
		_client.clean();
		return this.response();
	}

	public WebResponse install() throws IOException {
		_client.install();
		return this.response();
	}

	public WebResponse reinstall() throws IOException {
		_client.clean();
		_client.install();
		return this.response();
	}

	public WebResponse auto() throws IOException {
		_client.clean();
		_client.check();
		_client.download();
		_client.install();
		return this.response();
	}

	private WebResponse response() {
		WebResponse res = new WebResponse();
		res.setMeta(_client.getMeta());
		return res;
	}

}
