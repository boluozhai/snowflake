package com.boluozhai.snowflake.test;

import java.io.File;

import com.boluozhai.snow.mvc.controller.ProcessContext;
import com.boluozhai.snowflake.appdata.AppData;

public interface TestContext extends ProcessContext {

	AppData getAppData();

	Object getTestTarget();

	File getWorkingPath();

	File getWorksTemplatePath();

}
