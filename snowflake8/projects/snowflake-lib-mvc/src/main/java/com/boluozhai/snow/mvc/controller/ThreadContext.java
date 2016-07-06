package com.boluozhai.snow.mvc.controller;

import com.boluozhai.snow.mvc.ControllerContext;

public interface ThreadContext extends ControllerContext {

	ProcessContext getOwnerProcess();

	ThreadContext getParentThread();

}
