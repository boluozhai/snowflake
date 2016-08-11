package com.boluozhai.snowflake.libwebapp.update;

import java.io.IOException;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.libwebapp.update.impl.DefaultUpdatePublisherKit;
import com.boluozhai.snowflake.xgit.ObjectId;

public interface UpdatePublisherKit extends UpdateKit {

	public static class Agent {

		public static UpdatePublisherKit getInstance(SnowContext context) {
			return new DefaultUpdatePublisherKit(context);
		}

	}

	void add();

	void commit();

	void push();

	WebappSet loadWebappPomList();

	/*********
	 * @return the commit hash id
	 * @throws IOException
	 * */

	ObjectId addWebappsToRepository(WebappSet webapps) throws IOException;

	void locateWarFiles(WebappSet webapp_set);

}
