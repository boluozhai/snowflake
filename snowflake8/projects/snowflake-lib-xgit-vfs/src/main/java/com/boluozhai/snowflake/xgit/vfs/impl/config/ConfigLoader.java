package com.boluozhai.snowflake.xgit.vfs.impl.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snow.util.IOTools;
import com.boluozhai.snow.util.TextTools;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;

public class ConfigLoader {

	private final ComponentContext context;

	public ConfigLoader(ComponentContext context) {
		this.context = context;
	}

	public Map<String, String> load(VFile file) throws IOException {
		InputStream in = null;
		try {
			VFSIO io = VFSIO.Agent.getInstance(this.context);
			in = io.input(file);
			String str = TextTools.load(in);
			Builder builder = new Builder();
			builder.setText(str);
			return builder.create();
		} finally {
			IOTools.close(in);
		}
	}

	private class Builder {

		private final Map<String, String> table;
		private String segment;

		public Builder() {
			this.table = new HashMap<String, String>();
		}

		public void setText(String s) {
			StringBuilder sb = new StringBuilder();
			char[] chs = s.toCharArray();
			for (char ch : chs) {
				if (ch == 0x0a || ch == 0x0d) {
					this.onLine(sb);
				} else {
					sb.append(ch);
				}
			}
			this.onLine(sb);
		}

		private void onLine(StringBuilder sb) {
			if (sb.length() > 0) {
				this.onLine(sb.toString().trim());
				sb.setLength(0);
			}
		}

		private void onLine(String s) {
			if (s.startsWith("[")) {
				this.onLineSegment(s);
			} else {
				this.onLineKV(s);
			}
		}

		private void onLineSegment(String s) {

			if (s.startsWith("[") && s.endsWith("]")) {
				// OK
			} else {
				String msg = "bad git/config format: " + s;
				throw new RuntimeException(msg);
			}

			final int i1 = s.indexOf('"');
			if (i1 < 0) {
				// no ["]
				this.segment = s.substring(1, s.length() - 1).trim();
			} else {
				final int i2 = s.indexOf('"', i1 + 1);
				if (i2 < 0) {
					String msg = "bad git/config format: " + s;
					throw new RuntimeException(msg);
				}
				String s1 = s.substring(1, i1).trim();
				String s2 = s.substring(i1 + 1, i2).trim();
				this.segment = s1 + '.' + s2;
			}

		}

		private void onLineKV(String s) {
			final int i = s.indexOf('=');
			if (i < 0) {
				return;
			}
			String key = s.substring(0, i).trim();
			final String value = s.substring(i + 1).trim();
			final String seg = this.segment;
			if (seg != null) {
				key = seg + '.' + key;
			}
			table.put(key, value);
		}

		public Map<String, String> create() {
			return table;
		}
	}

}
