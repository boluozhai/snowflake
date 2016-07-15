package com.boluozhai.snowflake.xgit.vfs.impl.config;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.boluozhai.snow.util.IOTools;
import com.boluozhai.snow.util.TextTools;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;

final class ConfigSaver {

	private final ComponentContext context;

	public ConfigSaver(ComponentContext context) {
		this.context = context;
	}

	public void save(Map<String, String> table, VFile file) throws IOException {
		TextBuilder tb = new TextBuilder();
		Set<Entry<String, String>> list = table.entrySet();
		for (Entry<String, String> entry : list) {
			String key = entry.getKey();
			String value = entry.getValue();
			tb.append(key, value);
		}
		String text = tb.build();
		OutputStream out = null;
		try {
			VFSIO io = VFSIO.Agent.getInstance(context);
			out = io.output(file);
			TextTools.save(text, out);
		} finally {
			IOTools.close(out);
		}
	}

	private class Segment {

		private final String _seg_key;
		private final Map<String, String> _data;

		public Segment(String key) {
			this._seg_key = key;
			this._data = new HashMap<String, String>();
		}

		public void put(String key, String value) {
			this._data.put(key, value);
		}

		public void build(StringBuilder sb) {

			// head
			final String sk = this._seg_key;
			if (sk != null) {
				sb.append('[');
				final int i = sk.indexOf('.');
				if (i < 0) {
					sb.append(sk);
				} else {
					String k1 = sk.substring(0, i);
					String k2 = sk.substring(i + 1);
					sb.append(k1).append(" \"").append(k2).append('"');
				}
				sb.append("]\n");
			}

			// body
			List<String> keys = new ArrayList<String>(this._data.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				String value = this._data.get(key);
				sb.append('\t').append(key).append('=').append(value)
						.append('\n');
			}

		}

	}

	private class TextBuilder {

		private final Map<String, Segment> seg_more;
		private Segment seg_default;

		private TextBuilder() {
			this.seg_more = new HashMap<String, Segment>();
		}

		public void append(String key, String value) {
			final String k1, k2;
			final int i = key.lastIndexOf('.');
			if (i < 0) {
				k1 = null;
				k2 = key;
			} else {
				k1 = key.substring(0, i);
				k2 = key.substring(i + 1);
			}
			Segment seg = this.getSegment(k1);
			seg.put(k2, value);
		}

		private Segment getSegment(String key) {
			Segment seg = null;
			if (key == null) {
				seg = this.seg_default;
				if (seg == null) {
					seg = new Segment(key);
					this.seg_default = seg;
				}
			} else {
				seg = this.seg_more.get(key);
				if (seg == null) {
					seg = new Segment(key);
					this.seg_more.put(key, seg);
				}
			}
			return seg;
		}

		public String build() {

			StringBuilder sb = new StringBuilder();

			Segment seg = this.seg_default;
			if (seg != null) {
				seg.build(sb);
			}

			List<String> keys = new ArrayList<String>(this.seg_more.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				seg = this.seg_more.get(key);
				seg.build(sb);
			}

			return sb.toString();
		}
	}

}
