package com.boluozhai.snowflake.xgit.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamReader.Parameter;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class XGitCat extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		// parameter

		String idstr = null;
		Builder builder = ParamReader.newBuilder();
		ParamReader reader = builder.create(context);
		for (;;) {
			Parameter p = reader.read();
			if (p == null) {
				break;
			} else if (!p.isOption) {
				if (idstr == null) {
					idstr = p.value;
				}
			}
		}

		ObjectId id = ObjectId.Factory.create(idstr);

		// response

		CLIResponse resp = CLIResponse.Agent.getResponse(context);
		PrintStream out = resp.out();

		// find object

		RepositoryAgent agent = RepositoryAgent.Factory.get(context);
		Repository repo = agent.getRepository(context);
		XGitContext cc = repo.context();
		ObjectBank bank = (ObjectBank) cc
				.getBean(XGitContext.component.objects);
		GitObject obj = bank.object(id);
		GitObjectEntity ent = obj.entity();

		long length = obj.length();
		long limit = 1024 * 64;

		if (length > limit) {
			throw new RuntimeException(
					"the size of object is too large(>64k): " + length);
		}

		// output

		out.println("id: " + id);
		out.println("type: " + obj.type());
		out.println("plain-size: " + length);
		out.println("zipped-size: " + obj.zippedSize());
		out.println("content:");

		InputStream in = null;
		try {
			in = ent.openPlainInput();
			IOTools.pump(in, out);
		} catch (IOException e) {
			e.printStackTrace(out);
		} finally {
			IOTools.close(in);
			out.println();
			out.println("-- EOF --");
		}

	}

}
