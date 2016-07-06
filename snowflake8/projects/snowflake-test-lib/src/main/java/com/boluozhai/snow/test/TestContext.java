package com.boluozhai.snow.test;

import java.io.File;

import com.boluozhai.snow.mvc.controller.ProcessContext;

public interface TestContext extends ProcessContext {

	File getWorkingPath();

}
