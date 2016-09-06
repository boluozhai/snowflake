package com.boluozhai.snowflake.h2o.command;

import java.io.PrintStream;
import java.net.URI;
import java.util.Arrays;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;

public class CmdLS implements CLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		CLIResponse resp = CLIResponse.Agent.getResponse(context);
		PrintStream out = resp.out();

		URI uri = context.getURI();
		VFS vfs = VFS.Factory.getVFS(context);
		VFile path = vfs.newFile(uri);

		out.format("List items in directory [%s]\n", path);

		String[] list = path.list();
		Arrays.sort(list);
		for (String name : list) {
			out.format("  %s\n", name);
		}

	}

}
