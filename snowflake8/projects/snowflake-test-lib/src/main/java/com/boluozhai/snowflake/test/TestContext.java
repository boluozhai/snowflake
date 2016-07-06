package com.boluozhai.snowflake.test;

import java.io.File;

import com.boluozhai.snow.mvc.controller.ProcessContext;

public interface TestContext extends ProcessContext {

	File getWorkingPath();

}
