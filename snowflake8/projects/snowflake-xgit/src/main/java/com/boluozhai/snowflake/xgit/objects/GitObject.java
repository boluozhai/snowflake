package com.boluozhai.snowflake.xgit.objects;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface GitObject {

	interface TYPE {

		// for regular git

		String blob = "blob";
		String tree = "tree";
		String commit = "commit";
		String tag = "tag";

		// for xgit

		String blob_fragment = "blobfragment";
		String commit_section = "commitsection";
		String descriptor = "descriptor";

	}

	ObjectBank owner();

	ObjectId id();

	String type();

	long length();

	long zippedSize();

	GitObjectEntity entity();

	boolean exists();

	void delete();

}
