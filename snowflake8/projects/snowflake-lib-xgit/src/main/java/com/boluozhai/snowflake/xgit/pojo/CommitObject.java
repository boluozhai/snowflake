package com.boluozhai.snowflake.xgit.pojo;

import com.boluozhai.snowflake.xgit.ObjectId;

public class CommitObject extends CommitLikedTextObject {

	public interface KEY {

		String author = "author";
		String committer = "committer";
		String parent = "parent";
		String tree = "tree";

		String time = "time"; // not standard

	}

	public void setAuthor(Operator op) {
		String s = op.toString();
		this.setHeaderValue(KEY.author, s);
	}

	public void setCommitter(Operator op) {
		String s = op.toString();
		this.setHeaderValue(KEY.committer, s);
	}

	public void setTree(ObjectId id) {
		this.setHeaderValue(KEY.tree, id.toString());
	}

	public void addParent(ObjectId id) {
		this.addHeaderValue(KEY.parent, id.toString());
	}

	public void setTime(long time) {
		this.setHeaderValue(KEY.time, String.valueOf(time / 1000));
	}

	public long getTime() {
		String s = this.getHeaderValue(KEY.time);
		if (s == null) {
			return 0;
		}
		long time = Long.parseLong(s);
		return time * 1000;
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
