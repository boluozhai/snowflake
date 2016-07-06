package com.boluozhai.snow.mvc.controller;

import com.boluozhai.snow.mvc.ControllerContext;

public interface FragmentContext extends ControllerContext {

	ProcessContext getOwnerProcess();

	ThreadContext getOwnerThread();

}
