package com.boluozhai.snowflake.libwebapp.update.publisher.command;

import java.io.IOException;
import java.util.Map;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.libwebapp.update.UpdatePublisherKit;
import com.boluozhai.snowflake.xgit.ObjectId;

public class CmdSnowUpdatePublisherLoadapps implements CLICommandHandler {

	@Override
	public void process(SnowContext context, String command) {

		// TODO Auto-generated method stub

		try {

			System.out.println("load apps war to repo ...");

			UpdatePublisherKit kit = UpdatePublisherKit.Agent
					.getInstance(context);

			WebappSet webapp_set = kit.loadWebappPomList();
			kit.locateWarFiles(webapp_set);
			ObjectId commit = kit.addWebappsToRepository(webapp_set);

			Map<String, WebappInfo> list = webapp_set.getApps();
			for (WebappInfo info : list.values()) {
				System.out.println(info.getWarFileURI());
			}

		} catch (IOException e) {

			throw new RuntimeException(e);

		} finally {
		}

	}

}
