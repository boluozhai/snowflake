package com.boluozhai.snowflake.test;

import java.io.File;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.mvc.controller.ProcessContext;

public interface TestContext extends ProcessContext {

	AppData getAppData();

	Object getTestTarget();

	File getWorkingPath();

	File getWorksTemplatePath();

}
