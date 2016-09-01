package com.boluozhai.snowflake.xgit.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitLikedTextObject;
import com.boluozhai.snowflake.xgit.pojo.CommitLikedTextObject.Header;

public class CommitLikedTextObjectDAO {

	public static CommitLikedTextObject load(GitObject go,
			CommitLikedTextObject pojo) throws IOException {

		InputStream in = null;
		try {
			GitObjectEntity ent = go.entity();
			in = ent.openPlainInput();
			String s = TextTools.load(in);
			TextObjectBuilder builder = new TextObjectBuilder(pojo);
			builder.parse(s);
			return pojo;
		} finally {
			IOTools.close(in);
		}
	}

	public static GitObject save(CommitLikedTextObject pojo, ObjectBank bank)
			throws IOException {
		TextObjectSaver saver = new TextObjectSaver();
		return saver.save2bank(bank, pojo);
	}

	private static class TextObjectSaver {

		public String toString(CommitLikedTextObject pojo) {

			// to list
			List<String> list = new ArrayList<String>();
			Map<String, Header> head = pojo.getHead();
			for (String key : head.keySet()) {
				Header hdr = head.get(key);
				final String val = hdr.getValue();
				final List<String> vs = hdr.getValues();
				if (vs != null) {
					for (String value : vs) {
						list.add(String.format("%s %s", key, value));
					}
				} else if (val != null) {
					list.add(String.format("%s %s", key, val));
				} else {
					// NOP
				}
			}
			Collections.sort(list);

			// to string
			final String ln = "\n";
			final StringBuilder sb = new StringBuilder();
			for (String line : list) {
				sb.append(line).append(ln);
			}
			sb.append(ln);
			String body = pojo.getBody();
			sb.append((body == null) ? "" : body);

			return sb.toString();
		}

		public GitObject save2bank(ObjectBank bank, CommitLikedTextObject pojo)
				throws IOException {
			String txt = this.toString(pojo);
			byte[] ba = this.toByteArray(txt);
			return this.save2bank(bank, ba, pojo.getType());
		}

		public GitObject save2bank(ObjectBank bank, byte[] ba, String type)
				throws IOException {
			GitObjectBuilder builder = bank.newBuilder(type, ba.length);
			builder.write(ba, 0, ba.length);
			return builder.create();
		}

		public byte[] toByteArray(String txt)
				throws UnsupportedEncodingException {
			String enc = "utf-8";
			return txt.getBytes(enc);
		}

	}

	private static class TextObjectBuilder {

		private final CommitLikedTextObject pojo;

		public TextObjectBuilder(CommitLikedTextObject pojo) {
			this.pojo = pojo;
		}

		public void parse(String s) {
			int offset = 0;
			final char[] chs = s.toCharArray();
			for (int i = 0; i < chs.length; i++) {
				final char ch = chs[i];
				if (ch == '\n' || ch == '/') {
					final String line = new String(chs, offset, i - offset);
					offset = i + 1;
					if (line.length() == 0) {
						// end of head
						break;
					} else {
						this.on_head_line(line);
					}
				}
			}
			final String body;
			if (offset < chs.length) {
				body = new String(chs, offset, chs.length - offset);
			} else {
				body = "";
			}
			pojo.setBody(body);
		}

		private void on_head_line(String line) {
			final int i = line.indexOf(' ');
			if (i < 0) {
				return;
			}
			String k = line.substring(0, i).trim();
			String v = line.substring(i + 1).trim();
			pojo.addHeaderValue(k, v);
		}
	}

}
