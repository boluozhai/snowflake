package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.util.TextTools;
import com.boluozhai.snowflake.vfs.VFSContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnore;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnoreItem;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;

public class InnerGitIgnore4dir implements GitIgnore {

	private final ScanningNode _node;
	private final VFile _node_path;
	private final VFile _gitignore_file;
	private final GitIgnore _parent;
	private final boolean _ignored;
	private final GitIgnoreItem[] _items;
	private final GitIgnoreItem[] _inherited_items;
	private final GitIgnoreItem[] _defined_items;

	private static class GitignoreFileLoader {

		public GitIgnoreItem[] load(VFile file) throws IOException {
			InputStream in = null;
			try {
				VFSContext context = file.vfs().context();
				VFSIO io = VFSIO.Agent.getInstance(context);
				in = io.input(file);
				String text = TextTools.load(in);
				String[] lines = TextTools.splitToLines(text);
				List<GitIgnoreItem> list = new ArrayList<GitIgnoreItem>();
				this.parseLines(lines, list);
				return list.toArray(new GitIgnoreItem[list.size()]);
			} finally {
				IOTools.close(in);
			}
		}

		private void parseLines(String[] lines, List<GitIgnoreItem> list) {
			for (String line : lines) {
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				String[] elements = this.parseLine2elements(line);
				GitIgnoreItem chain = this.makeChain(elements);
				if (chain != null) {
					list.add(chain);
				}
			}
		}

		private GitIgnoreItem makeChain(String[] elements) {
			GitIgnoreItem head = null;
			for (int i = elements.length - 1; i >= 0; i--) {
				String name = elements[i];
				head = new InnerGitIgnoreItem(name, head);
			}
			return head;
		}

		private String[] parseLine2elements(String line) {
			List<String> list = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			char[] chs = line.toCharArray();
			for (char ch : chs) {
				if (ch == '/' || ch == '\\') {
					if (sb.length() > 0) {
						this.flush(sb, list);
					}
				} else {
					sb.append(ch);
				}
			}
			this.flush(sb, list);
			return list.toArray(new String[list.size()]);
		}

		private void flush(StringBuilder from, List<String> to) {
			if (from.length() < 1) {
				return;
			}
			String s = from.toString().trim();
			from.setLength(0);
			if (s.length() > 0) {
				to.add(s);
			}
		}

	}

	private static class Loader {

		public final ScanningNode node;
		private GitIgnoreItem[] _inh_items;
		private GitIgnoreItem[] _def_items;
		private VFile _gitignore_file;

		public Loader(ScanningNode node0) {
			this.node = node0;
		}

		public GitIgnore load() {
			return new InnerGitIgnore4dir(this);
		}

		public GitIgnoreItem[] all_items() {

			final GitIgnoreItem[] a1 = this._inh_items;
			final GitIgnoreItem[] a2 = this._def_items;
			final int len1 = a1.length;
			final int len2 = a2.length;
			if (len1 + len2 <= 0) {
				return Tools.nil;
			}

			final GitIgnoreItem[] a3 = new GitIgnoreItem[len1 + len2];
			for (int i = len1 - 1; i >= 0; i--) {
				a3[i] = a1[i];
			}
			for (int i = len2 - 1; i >= 0; i--) {
				a3[len1 + i] = a2[i];
			}
			return a3;
		}

		public GitIgnoreItem[] def_items() {
			GitIgnoreItem[] array = Tools.nil;
			try {
				VFile file = this._gitignore_file;
				if (file == null) {
					return Tools.nil;
				}
				if (!file.exists()) {
					return Tools.nil;
				}
				GitignoreFileLoader ldr = new GitignoreFileLoader();
				array = ldr.load(file);
				return array;
			} catch (IOException e) {
				e.printStackTrace();
				return array;
			} finally {
				this._def_items = array;
			}
		}

		public GitIgnoreItem[] inh_items() {
			GitIgnoreItem[] array = Tools.nil;
			try {

				GitIgnore parent = this.parent();
				if (parent == null) {
					return Tools.nil;
				}
				GitIgnoreItem[] items = parent.items();
				if (items == null) {
					return Tools.nil;
				}
				if (items.length < 1) {
					return Tools.nil;
				}

				String name = this.node.getName();
				List<GitIgnoreItem> list = new ArrayList<GitIgnoreItem>();
				for (GitIgnoreItem item : items) {
					String n2 = item.getName();
					if (Tools.is_name_match(name, n2) && item.hasMore()) {
						list.add(item.next());
					}
					if (n2.equals("*")) {
						list.add(item);
					}
				}

				array = list.toArray(new GitIgnoreItem[list.size()]);
				return array;

			} finally {
				this._inh_items = array;
			}
		}

		public boolean ignored() {
			GitIgnore parent = this.parent();
			if (parent == null) {
				return false;
			} else {
				String name = this.node.getName();
				return parent.ignore(name);
			}
		}

		public GitIgnore parent() {
			ScanningNode parent = this.node.parent();
			if (parent == null) {
				return null;
			} else {
				return parent.getIgnore();
			}
		}

		public VFile node_file() {
			return this.node.getFile();
		}

		public ScanningNode node() {
			return this.node;
		}

		public VFile gitignore_file() {
			VFile file = null;
			try {
				VFile dir = this.node.getFile();
				String name = ".gitignore";
				file = dir.child(name);
				if (file.exists()) {
					return file;
				}
				name = ".xgitignore";
				file = dir.child(name);
				if (file.exists()) {
					return file;
				}
				file = null;
				return file;
			} finally {
				this._gitignore_file = file;
			}
		}

	}

	private InnerGitIgnore4dir(Loader ldr) {

		this._node = ldr.node();
		this._node_path = ldr.node_file();
		this._gitignore_file = ldr.gitignore_file();
		this._parent = ldr.parent();
		this._ignored = ldr.ignored();

		this._defined_items = ldr.def_items();
		this._inherited_items = ldr.inh_items();
		this._items = ldr.all_items();

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

		public static boolean is_name_match(String n1, String n2) {

			if (n1 == null || n2 == null) {
				return false;
			}

			if (n1.equals(n2)) {
				return true;
			}

			if (n1.equals("*") || n2.equals("*")) {
				return true;
			}

			// TODO match like 'a*b*c'

			return false;
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

	@Override
	public boolean ignore(String name) {
		GitIgnoreItem[] items = this.items();
		for (GitIgnoreItem item : items) {
			if (item.hasMore()) {
				continue;
			} else {
				String n2 = item.getName();
				if (Tools.is_name_match(name, n2)) {
					return true;
				}
			}
		}
		return false;
	}

}
