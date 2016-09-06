package com.boluozhai.snowflake.h2o.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIClient;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.h2o.utils.RestCommandAdapter;
import com.boluozhai.snowflake.libwebapp.utils.WebContextUtils;
import com.boluozhai.snowflake.rest.api.h2o.CommandModel;
import com.boluozhai.snowflake.rest.element.command.CommandInfo;
import com.boluozhai.snowflake.rest.server.JsonRestView;
import com.boluozhai.snowflake.rest.server.RestController;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.google.gson.Gson;

public class CommandCtrl extends RestController {

	@Override
	protected void rest_post(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JsonRestView view = new JsonRestView();

		try {

			ServletContext sc = request.getServletContext();
			SnowflakeContext context = WebContextUtils.getWebContext(sc);

			// load
			CommandModel model = this.load_request_entity(request);
			CommandInfo arg = model.getArgument();
			CommandInfo result = new CommandInfo();
			model.setResult(result);
			view.setResponsePOJO(model);

			// adapter
			final VFS vfs = VFS.Factory.getVFS(context);
			final VFile path = vfs.newFile(URI.create(arg.getPathURI()));
			RestCommandAdapter adapter = new RestCommandAdapter(context);
			adapter.setPath(path);
			context = adapter.createChildContext();

			// execute
			this.exec(context, model);

			// complete
			String result_text = adapter.getResultText();
			result.setMessage(result_text);
			result.setPathURI(path.toURI().toString());
			result.setCommand(arg.getCommand());
			view.forward(request, response);

		} finally {
		}

	}

	private void exec(SnowflakeContext context, CommandModel model) {

		CommandInfo arg = model.getArgument();
		CommandInfo rlt = model.getResult();
		String cmd = arg.getCommand();

		CLIClient client = CLIUtils.getClient(context);
		client.execute(context, cmd);
		rlt.setMessage("ok");

	}

	private CommandModel load_request_entity(HttpServletRequest request)
			throws IOException {
		InputStream in = null;
		try {
			in = request.getInputStream();
			String str = TextTools.load(in);
			Gson gs = new Gson();
			return gs.fromJson(str, CommandModel.class);
		} finally {
			IOTools.close(in);
		}
	}

}
