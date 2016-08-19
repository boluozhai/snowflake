package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnore;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnoreItem;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;

public class InnerGitIgnore implements GitIgnore {

	private final ScanningNode _node;
	private final VFile _node_path;
	private final VFile _gitignore_file;
	private final GitIgnore _parent;
	private final boolean _ignored;
	private final GitIgnoreItem[] _items;
	private final GitIgnoreItem[] _inherited_items;
	private final GitIgnoreItem[] _defined_items;

	private static class Loader {

		public final ScanningNode node; // the node of .gitignore's parent
		public final VFile gitignore_file; // the path of .gitignore
		public final VFile node_file; // the path of node

		public boolean ignored;
		public GitIgnore parent;

		public GitIgnoreItem[] all_items;
		public GitIgnoreItem[] def_items;
		public GitIgnoreItem[] inh_items;

		public Loader(ScanningNode node0) {

			VFile nf = node0.getFile();

			this.node = node0;
			this.node_file = nf;
			this.gitignore_file = nf.child(".gitignore");

		}

		public GitIgnore load() {

			// TODO Auto-generated method stub

			ScanningNode parent_node = node.parent();

			if (parent_node == null) {
			} else {
				this.parent = parent_node.getIgnore();
				if (parent.isIgnored()) {
					this.ignored = true;
				}
			}

			return new InnerGitIgnore(this);

		}

	}

	private InnerGitIgnore(Loader ldr) {

		this._node = ldr.node;
		this._node_path = ldr.node_file;
		this._gitignore_file = ldr.gitignore_file;
		this._ignored = ldr.ignored;
		this._parent = ldr.parent;

		this._items = Tools.make_copy(ldr.all_items);
		this._defined_items = Tools.make_copy(ldr.def_items);
		this._inherited_items = Tools.make_copy(ldr.inh_items);

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
		return this._gitignore_file;
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

		private static final GitIgnoreItem[] nil = {};

		public static GitIgnoreItem[] make_copy(GitIgnoreItem[] items) {
			if (items == null) {
				return nil;
			} else if (items.length == 0) {
				return nil;
			} else {
				final int len = items.length;
				GitIgnoreItem[] array = new GitIgnoreItem[len];
				for (int i = len - 1; i >= 0; i--) {
					array[i] = items[i];
				}
				return array;
			}
		}

	}

	@Override
	public GitIgnoreItem[] items() {
		return Tools.make_copy(this._items);
	}

	@Override
	public GitIgnoreItem[] definedItems() {
		return Tools.make_copy(this._defined_items);
	}

	@Override
	public GitIgnoreItem[] inheritedItems() {
		return Tools.make_copy(this._inherited_items);
	}

}
