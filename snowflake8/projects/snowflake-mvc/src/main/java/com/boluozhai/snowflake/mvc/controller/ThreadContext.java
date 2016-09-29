package com.boluozhai.snowflake.mvc.controller;

import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.mvc.ControllerContext;

public interface ThreadContext extends ControllerContext, MutableContext {

	ProcessContext getOwnerProcess();

	ThreadContext getParentThread();

}
