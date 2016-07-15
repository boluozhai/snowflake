package com.boluozhai.snowflake.mvc.controller;

import com.boluozhai.snowflake.mvc.ControllerContext;

public interface FragmentContext extends ControllerContext {

	ProcessContext getOwnerProcess();

	ThreadContext getOwnerThread();

}
