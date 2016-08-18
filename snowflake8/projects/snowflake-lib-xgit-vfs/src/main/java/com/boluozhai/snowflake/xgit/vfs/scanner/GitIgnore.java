package com.boluozhai.snowflake.xgit.vfs.scanner;

import com.boluozhai.snowflake.vfs.VFileNode;

/****
 * this class delegate the .gitignore file
 * */

public interface GitIgnore extends VFileNode {

	GitIgnore getParent();

	ScanningNode getNode();

	boolean isIgnored();

	/***
	 * all items
	 * */

	GitIgnoreItem[] items();

	/****
	 * the items in the .gitignore file
	 * */

	GitIgnoreItem[] definedItems();

	/****
	 * the items inherit from parent
	 * */

	GitIgnoreItem[] inheritedItems();

}
