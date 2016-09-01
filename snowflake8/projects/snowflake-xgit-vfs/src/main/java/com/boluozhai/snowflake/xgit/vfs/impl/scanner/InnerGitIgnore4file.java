package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnore;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnoreItem;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;

public class InnerGitIgnore4file implements GitIgnore {

	private final ScanningNode _node;
	private final VFile _node_path;
	private final GitIgnore _parent;
	private final boolean _ignored;

	private static class Loader {

		public final ScanningNode node;

		public Loader(ScanningNode node0) {
			this.node = node0;
		}

		public GitIgnore load() {
			return new InnerGitIgnore4file(this);
		}

		public VFile get_node_path() {
			return node.getFile();
		}

		public boolean is_ignored() {
			String name = node.getName();
			GitIgnore parent = this.get_parent();
			if (parent != null) {
				return parent.ignore(name);
			} else {
				return false;
			}
		}

		public GitIgnore get_parent() {
			ScanningNode parent = node.parent();
			if (parent == null) {
				return null;
			} else {
				return parent.getIgnore();
			}
		}

	}

	private InnerGitIgnore4file(Loader ldr) {
		this._node = ldr.node;
		this._node_path = ldr.get_node_path();
		this._ignored = ldr.is_ignored();
		this._parent = ldr.get_parent();
	}

	public static GitIgnore open(ScanningNode node) {
		Loader ldr = new Loader(node);
		return ldr.load();
	}

	@Override
	public VFile getFile() {
		return this._node_path;
	}

	@Override
	public VFile getGitignoreFile() {
		return null;
	}

	@Override
	public GitIgnore getParent() {
		return this._parent;
	}

	@Override
	public ScanningNode getNode() {
		return this._node;
	}

	@Override
	public boolean isIgnored() {
		return this._ignored;
	}

	private static class Tools {

		private static final GitIgnoreItem[] empty = {};

	}

	@Override
	public GitIgnoreItem[] items() {
		return Tools.empty;
	}

	@Override
	public GitIgnoreItem[] definedItems() {
		return Tools.empty;
	}

	@Override
	public GitIgnoreItem[] inheritedItems() {
		return Tools.empty;
	}

	@Override
	public boolean ignore(String name) {
		return false;
	}

}
