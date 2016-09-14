package com.boluozhai.snowflake.xgit.http.client.toolkit;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.xgit.http.client.impl.toolkit.ToolkitImplementation;

public interface GitClientToolkit {

	class Factory {

		public static GitClientToolkit newInstance(SnowflakeContext context) {
			return ToolkitImplementation.createToolkit(context);
		}

	}

	RemoteAgent getRemote(String url);

}
