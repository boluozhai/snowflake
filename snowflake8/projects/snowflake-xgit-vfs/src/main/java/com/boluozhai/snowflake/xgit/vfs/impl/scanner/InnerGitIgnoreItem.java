package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnoreItem;

final class InnerGitIgnoreItem implements GitIgnoreItem {

	private final String _name;
	private final GitIgnoreItem _next;

	public InnerGitIgnoreItem(String name, GitIgnoreItem next) {
		this._name = name;
		this._next = next;
	}

	@Override
	public boolean hasMore() {
		return (_next != null);
	}

	@Override
	public GitIgnoreItem next() {
		return _next;
	}

	@Override
	public String getName() {
		return _name;
	}

}
