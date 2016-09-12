package com.boluozhai.snowflake.cli.impl;

import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.cli.CLIUtils;
import com.boluozhai.snowflake.cli.client.CLIProcess;
import com.boluozhai.snowflake.cli.client.ExecuteOption;
import com.boluozhai.snowflake.cli.service.CLIService;
import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.utils.ContextBuilderConfigHelper;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;

final class CLIProcessBuilder {

	private boolean autoStart;
	private boolean autoJoin;
	private String command;
	private ExecuteOption option;
	private String[] arguments;
	private final SnowflakeContext context;

	public CLIProcessBuilder(SnowflakeContext context) {
		this.context = context;
	}

	public CLIProcess create() {

		String fn = SnowContextUtils.FactoryName.child;
		ContextBuilder context_builder = SnowContextUtils.getContextBuilder(
				context, fn);
		MyContextBuilderWrapper cbw = new MyContextBuilderWrapper(
				context_builder);
		cbw.clearParameters();

		String cmd = cbw.loadCommand(command);
		cbw.loadOption(option);
		cbw.loadArguments(arguments);

		SnowflakeContext sub_context = context_builder.create();

		CLIService service = CLIUtils.getService(sub_context);
		CLICommandHandler handler = service.getHandler(cmd);

		if (handler == null) {
			String msg = "cannot find cli command: " + cmd;
			throw new RuntimeException(msg);
		}

		MyCLIProcess proc = new MyCLIProcess(sub_context, handler, cmd);

		if (this.autoStart) {
			if (this.autoJoin) {
				proc.run();
			} else {
				proc.start();
			}
		}

		return proc;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public ExecuteOption getOption() {
		return option;
	}

	public void setOption(ExecuteOption option) {
		this.option = option;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	public boolean isAutoJoin() {
		return autoJoin;
	}

	public void setAutoJoin(boolean autoJoin) {
		this.autoJoin = autoJoin;
	}

	private static class MyContextBuilderWrapper {

		private final ContextBuilder _builder;

		public MyContextBuilderWrapper(ContextBuilder builder) {
			this._builder = builder;
		}

		public void clearParameters() {
			this._builder.getParameters().clear();
		}

		public void loadArguments(String[] arguments) {
			ContextBuilderConfigHelper
					.config_app_arguments(_builder, arguments);
		}

		public void loadOption(ExecuteOption option) {
			// TODO Auto-generated method stub
		}

		public String loadCommand(String command) {
			if (command == null) {
				return null;
			} else {
				command = command.trim();
			}
			int index = command.indexOf(' ');
			if (index < 0) {
				return command;
			}
			String cmd = command.substring(0, index).trim();
			String arg = command.substring(index + 1).trim();
			String[] array = this.argumentString2array(arg);
			this.loadArguments(array);
			return cmd;
		}

		private String[] argumentString2array(String arg) {

			List<String> list = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			char ref_mark = 0;

			for (char ch : arg.toCharArray()) {
				if (ref_mark == 0) {
					// out of ref
					if (ch == ' ' || ch == '\t' || ch == '=') {
						this.flush_buffer_to_list(list, sb);
					} else if (ch == '\'' || ch == '"') {
						this.flush_buffer_to_list(list, sb);
						ref_mark = ch;
					} else {
						sb.append(ch);
					}
				} else {
					// in ref
					if (ch == ref_mark) {
						list.add(sb.toString());
						sb.setLength(0);
						ref_mark = 0;
					} else {
						sb.append(ch);
					}
				}
			}
			this.flush_buffer_to_list(list, sb);

			return list.toArray(new String[list.size()]);
		}

		private void flush_buffer_to_list(List<String> list, StringBuilder sb) {
			if (sb.length() > 0) {
				String s = sb.toString().trim();
				sb.setLength(0);
				if (s.length() > 0) {
					list.add(s);
				}
			}
		}

	}

	private static class MyCLIProcess implements CLIProcess {

		private Thread _thread;
		private final SnowflakeContext _context;
		private final CLICommandHandler _handler;
		private final String _command;

		public MyCLIProcess(SnowflakeContext context,
				CLICommandHandler handler, String cmd) {
			this._context = context;
			this._handler = handler;
			this._command = cmd;
		}

		@Override
		public void run() {
			try {
				System.out.format("exec: %s\n", _command);
				_handler.process(_context, _command);
			} catch (Exception e) {
				final RuntimeException re;
				if (e instanceof RuntimeException) {
					re = (RuntimeException) e;
				} else {
					re = new RuntimeException(e);
				}
				throw re;
			} finally {
			}
		}

		@Override
		public void start() {
			Thread thread = new Thread(this);
			this._thread = thread;
			thread.start();
		}

		@Override
		public void join() throws InterruptedException {
			this._thread.join();
		}

		@Override
		public SnowflakeContext context() {
			return this._context;
		}
	}

}
