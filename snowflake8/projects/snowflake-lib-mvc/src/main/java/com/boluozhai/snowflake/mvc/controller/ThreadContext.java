package com.boluozhai.snowflake.mvc.controller;

import com.boluozhai.snowflake.mvc.ControllerContext;

public interface ThreadContext extends ControllerContext {

	ProcessContext getOwnerProcess();

	ThreadContext getParentThread();

}
