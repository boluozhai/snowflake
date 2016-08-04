package com.boluozhai.snowflake.libwebapp.update;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.libwebapp.update.impl.DefaultUpdatePublisherKit;

public interface UpdatePublisherKit extends UpdateKit {

	public static class Agent {

		public static UpdatePublisherKit getInstance(SnowContext context) {
			return new DefaultUpdatePublisherKit(context);
		}

	}

	WebappSet loadWebappPomList();

	/*********
	 * @return the commit hash id
	 * */

	String addWebappsToRepository(WebappSet webapps);

	void locateWarFiles(WebappSet webapp_set);

}
