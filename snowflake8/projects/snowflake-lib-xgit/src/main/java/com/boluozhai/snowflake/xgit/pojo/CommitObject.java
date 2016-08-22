package com.boluozhai.snowflake.xgit.pojo;

import com.boluozhai.snowflake.xgit.ObjectId;

public class CommitObject extends CommitLikedTextObject {

	public interface KEY {

		String author = "author";
		String committer = "committer";
		String parent = "parent";
		String tree = "tree";

		// the non-standard fields

		String time = "time";// milliseconds from 1970-01-01
		String section = "section"; // the section base path

	}

	public void setAuthor(Operator op) {
		final String key = KEY.author;
		if (op == null) {
			this.setHeaderValue(key, null);
		} else {
			String s = op.toString();
			this.setHeaderValue(key, s);
		}
	}

	public void setCommitter(Operator op) {
		final String key = KEY.committer;
		if (op == null) {
			this.setHeaderValue(key, null);
		} else {
			String s = op.toString();
			this.setHeaderValue(key, s);
		}
	}

	public void setTree(ObjectId id) {
		this.setHeaderValue(KEY.tree, id.toString());
	}

	public void addParent(ObjectId id) {
		this.addHeaderValue(KEY.parent, id.toString());
	}

	public void setTime(long time) {
		this.setHeaderValue(KEY.time, String.valueOf(time));
	}

	public long getTime() {
		String s = this.getHeaderValue(KEY.time);
		if (s == null) {
			return 0;
		}
		return Long.parseLong(s);
	}

	public ObjectId getTree() {
		String s = this.getHeaderValue(KEY.tree);
		return ObjectId.Factory.create(s);
	}

	public ObjectId[] getParents() {
		String[] list = this.getHeaderValues(KEY.parent);
		if (list == null) {
			return null;
		}
		ObjectId[] ret = new ObjectId[list.length];
		for (int i = ret.length - 1; i >= 0; i--) {
			String s = list[i];
			ret[i] = ObjectId.Factory.create(s);
		}
		return ret;
	}

	public Operator getAuthor() {
		String s = this.getHeaderValue(KEY.author);
		if (s == null) {
			return null;
		}
		return Operator.parse(s);
	}

	public Operator getCommitter() {
		String s = this.getHeaderValue(KEY.committer);
		if (s == null) {
			return null;
		}
		return Operator.parse(s);
	}

}
