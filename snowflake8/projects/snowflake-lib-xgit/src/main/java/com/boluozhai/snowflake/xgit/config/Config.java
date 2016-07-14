package com.boluozhai.snowflake.xgit.config;

import java.io.IOException;

import com.boluozhai.snowflake.context.MutableProperties;
import com.boluozhai.snowflake.xgit.XGitComponent;

public interface Config extends XGitComponent, MutableProperties {

	void load() throws IOException;

	void save() throws IOException;

}
