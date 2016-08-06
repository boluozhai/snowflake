package com.boluozhai.snowflake.xgit.dao.impl;

import java.io.IOException;
import java.io.InputStream;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectEntity;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitLikedTextObject;

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

	public static GitObject save(CommitLikedTextObject pojo, ObjectBank bank) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not impl");
		// return null;
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
