package com.boluozhai.snowflake.xgit.http.client.impl.toolkit;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitClientToolkit;

public class ToolkitImplementation {

	public static GitClientToolkit createToolkit(SnowflakeContext context) {
		return new GitClientToolkitImpl(context);
	}

}
