package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.viewport.ViewportProfile;

public class ViewportModel extends RestDoc {

	private ViewportProfile viewport;

	public ViewportProfile getViewport() {
		return viewport;
	}

	public void setViewport(ViewportProfile viewport) {
		this.viewport = viewport;
	}

}
