package com.boluozhai.snowflake.xgit.command;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.context.SnowflakeContext;

public class GitTemp extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {
		// TODO Auto-generated method stub

		ParamReader.Builder prb = ParamReader.newBuilder();
		ParamReader reader = prb.create(context);
		System.out.println("Parameters:");
		for (;;) {
			Parameter p = reader.read();
			if (p == null) {
				break;
			} else {
				System.out.println("  " + p);
			}
		}

	}

}
