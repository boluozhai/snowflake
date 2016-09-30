package com.boluozhai.snowflake.xgit.http.client.smart.fetch;

import com.boluozhai.snowflake.xgit.ObjectId;

public class SmartFetchItem {

	private SmartFetchItemController controller;
	private final ObjectId id;
	private final SmartFetchModel model;

	public SmartFetchItem(SmartFetchModel model, ObjectId id) {
		this.id = id;
		this.model = model;
	}

	public SmartFetchItemController getController() {
		SmartFetchItemController ctrl = this.controller;
		if (ctrl == null) {
			ctrl = new DefaultSmartItemCtrl(this);
			this.controller = ctrl;
		}
		return ctrl;
	}

	public void setController(SmartFetchItemController controller) {
		this.controller = controller;
	}

	public ObjectId getId() {
		return id;
	}

	public SmartFetchModel getModel() {
		return model;
	}

}
