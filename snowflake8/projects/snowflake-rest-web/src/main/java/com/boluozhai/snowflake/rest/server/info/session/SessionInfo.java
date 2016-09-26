package com.boluozhai.snowflake.rest.server.info.session;

import com.boluozhai.snowflake.rest.api.h2o.SessionModel;

public interface SessionInfo {

	SessionModel getModel();

	void setModel(SessionModel model);

}
