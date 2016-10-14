package com.boluozhai.snowflake.pojo.bridge.merge;

public class DefaultMerge implements Merge {

	@Override
	public Object merge(MergeField from, MergeField to) {

		Object f = from.value();
		Object t = to.value();

		if (f == null) {
			return t;
		} else {
			return f;
		}

	}

}
