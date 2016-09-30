package com.boluozhai.snowflake.xgit.http.client.smart.fetch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class SmartFetchModel {

	private SmartFetchController controller;
	private final Map<ObjectId, SmartFetchItem> all;
	private final Map<ObjectId, SmartFetchItem> existsNot;
	private final ObjectBank bank;
	private final ObjectId beginId;
	private final ObjectId endId;
	private final GitHttpRepo remote;

	public SmartFetchModel(Repository local, GitHttpRepo remote,
			ObjectId begin, ObjectId end) {
		this.all = new HashMap<ObjectId, SmartFetchItem>();
		this.existsNot = new HashMap<ObjectId, SmartFetchItem>();
		this.bank = ObjectBank.Factory.getBank(local);
		this.beginId = begin;
		this.endId = end;
		this.remote = remote;
	}

	public SmartFetchController getController() {
		SmartFetchController ctrl = this.controller;
		if (ctrl == null) {
			ctrl = new DefaultSmartFetchCtrl(this);
			this.controller = ctrl;
		}
		return ctrl;
	}

	public void setController(SmartFetchController controller) {
		this.controller = controller;
	}

	public Map<ObjectId, SmartFetchItem> getAll() {
		return all;
	}

	public Map<ObjectId, SmartFetchItem> getExistsNot() {
		return existsNot;
	}

	public ObjectBank getBank() {
		return bank;
	}

	public ObjectId getBeginId() {
		return beginId;
	}

	public ObjectId getEndId() {
		return endId;
	}

	public GitHttpRepo getRemote() {
		return remote;
	}

	public void fire() {
		try {

			SmartFetchController ctrl = this.getController();
			ctrl.add(this.beginId);
			ctrl.load();

		} catch (IOException e) {
			throw new SnowflakeException(e);

		} finally {
			// NOP
		}
	}

}
