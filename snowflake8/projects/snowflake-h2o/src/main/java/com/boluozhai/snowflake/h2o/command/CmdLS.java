package com.boluozhai.snowflake.h2o.command;

import java.io.PrintStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;

public class CmdLS extends AbstractCLICommandHandler {

	private boolean showSize;
	private boolean showType;
	private boolean showTime;

	public boolean isShowSize() {
		return showSize;
	}

	public void setShowSize(boolean showSize) {
		this.showSize = showSize;
	}

	public boolean isShowType() {
		return showType;
	}

	public void setShowType(boolean showType) {
		this.showType = showType;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	@Override
	public void process(SnowflakeContext context, String command) {

		CLIResponse resp = CLIResponse.Agent.getResponse(context);
		PrintStream out = resp.out();

		URI uri = context.getURI();
		VFS vfs = VFS.Factory.getVFS(context);
		VFile path = vfs.newFile(uri);
		Date date = new Date();

		out.format("List items in directory [%s]\n", path);

		String[] list = path.list();
		Arrays.sort(list);
		for (String name : list) {
			VFile ch = path.child(name);
			out.append("  ");
			out.append(this.tableString(0, name, 20));
			if (this.showSize) {
				String str = String.valueOf(ch.length());
				if (ch.isDirectory()) {
					str = "";
				}
				out.append(this.tableString(8, str, 0));
			}
			if (this.showType) {
				String str = this.toTypeString(ch, name);
				out.append(this.tableString(6, str, 0));
			}
			if (this.showTime) {
				String str = this.toTimeString(ch, date);
				out.append("  ").append(str);
			}
			out.println();
		}

	}

	private String tableString(int n1, String str, int n2) {
		final StringBuilder sb = new StringBuilder();
		final int n = Math.max(n1, n2);
		int append = n - str.length();
		if (n1 < n2) {
			sb.append(str);
			for (; append > 0; append--) {
				sb.append(' ');
			}
		} else {
			for (; append > 0; append--) {
				sb.append(' ');
			}
			sb.append(str);
		}
		return sb.toString();
	}

	private String toTypeString(VFile ch, String name) {
		if (ch.isDirectory()) {
			return "&lt;DIR&gt;";
		}
		final int index = name.lastIndexOf('.');
		if (index < 0) {
			return "&lt;FILE&gt;";
		} else {
			return name.substring(index);
		}
	}

	private String toTimeString(VFile ch, Date date) {
		long time = ch.lastModified();
		date.setTime(time);
		return date.toString();
	}

}
