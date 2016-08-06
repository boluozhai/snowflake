package com.boluozhai.snowflake.xgit.pojo;

import java.util.List;

import com.boluozhai.snowflake.xgit.ObjectId;

public class CommitObject extends CommitLikedTextObject {

	public interface KEY {

		String tree = "tree";
		String parent = "parent";
		String time = "time";

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
		long time = Long.parseLong(s);
		return time * 1000;
	}

	public ObjectId getTree() {
		String s = this.getHeaderValue(KEY.tree);
		return ObjectId.Factory.create(s);
	}

	public ObjectId[] getParents() {
		List<String> list = this.getHeaderValues(KEY.parent);
		String[] array = list.toArray(new String[list.size()]);
		ObjectId[] ret = new ObjectId[array.length];
		for (int i = ret.length - 1; i >= 0; i--) {
			String s = array[i];
			ret[i] = ObjectId.Factory.create(s);
		}
		return ret;
	}

}
