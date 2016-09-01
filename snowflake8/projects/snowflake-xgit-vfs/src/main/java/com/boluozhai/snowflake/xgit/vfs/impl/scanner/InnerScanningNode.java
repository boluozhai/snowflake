package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScanner;
import com.boluozhai.snowflake.xgit.vfs.scanner.GitIgnore;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;

public class InnerScanningNode implements ScanningNode {

	private final FileScanner _scanner;
	private final VPath _path;
	private final String _name;
	private final ScanningNode _parent;

	private Object _user_data;
	private GitIgnore _ignore;
	private MyFlags _flags;

	public InnerScanningNode(FileScanner scanner, VPath path,
			ScanningNode parent) {
		this._scanner = scanner;
		this._path = path;
		this._name = path.file().getName();
		this._parent = parent;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public FileScanner getScanner() {
		return this._scanner;
	}

	@Override
	public VPath getPath() {
		return this._path;
	}

	@Override
	public Object getUserData() {
		Object ud = this._user_data;
		if (ud == null) {
			ud = this._scanner.getUserDataFactory().createUserData(this);
			this._user_data = ud;
		}
		return ud;
	}

	@Override
	public ScanningNode child(String name) {
		VPath child_path = _path.child(name);
		return new InnerScanningNode(_scanner, child_path, this);
	}

	@Override
	public VFile getFile() {
		return this._path.file();
	}

	@Override
	public ScanningNode parent() {
		return this._parent;
	}

	@Override
	public GitIgnore getIgnore() {
		GitIgnore ignore = this._ignore;
		if (ignore == null) {
			ignore = this.inner_load_ignore();
			this._ignore = ignore;
		}
		return ignore;
	}

	private GitIgnore inner_load_ignore() {
		if (this._path.file().isDirectory()) {
			return InnerGitIgnore4dir.open(this);
		} else {
			return InnerGitIgnore4file.open(this);
		}
	}

	private static class MyFlags {

		public final int value;

		public MyFlags(int i) {
			this.value = i;
		}

	}

	private MyFlags inner_load_flags() {

		int v1 = 0;
		int v2 = 0;

		// the value inherent from parent
		ScanningNode parent = this.parent();
		if (parent != null) {
			v1 = parent.flags();
		}

		// ignore
		if ((v1 & ScanningNode.FLAG_IGNORED) == 0)
			if (this.inner_check_ignored()) {
				v2 |= ScanningNode.FLAG_IGNORED;
			}

		// in works
		if ((v1 & ScanningNode.FLAG_IN_WORKSPACE) == 0)
			if (this.inner_check_in_works()) {
				v2 |= ScanningNode.FLAG_IN_WORKSPACE;
			}

		// in repo
		if ((v1 & ScanningNode.FLAG_IN_REPOSITORY) == 0)
			if (this.inner_check_in_repo()) {
				v2 |= ScanningNode.FLAG_IN_REPOSITORY;
			}

		return new MyFlags(v1 | v2);
	}

	private final static String[] array_check_in_repo = { ".git", ".xgit",
			".snow", ".snowflake" };

	private boolean inner_check_in_repo() {
		String name = this.getFile().getName();
		for (String s : array_check_in_repo) {
			if (name.equals(s)) {
				return true;
			}
		}
		return false;
	}

	private boolean inner_check_in_works() {
		FileScanner scanner = this._scanner;
		FileWorkspace works = scanner.getWorkspace();
		String base = works.getFile().toURI().toString();
		String full = this.getFile().toURI().toString();
		return full.startsWith(base);
	}

	private boolean inner_check_ignored() {
		return this.getIgnore().isIgnored();
	}

	private MyFlags inner_get_flags() {
		MyFlags flags = this._flags;
		if (flags == null) {
			flags = this.inner_load_flags();
			this._flags = flags;
		}
		return flags;
	}

	@Override
	public boolean isInRepository() {
		int value = this.inner_get_flags().value;
		return ((value & ScanningNode.FLAG_IN_REPOSITORY) > 0);
	}

	@Override
	public boolean isInWorkspace() {
		int value = this.inner_get_flags().value;
		return ((value & ScanningNode.FLAG_IN_WORKSPACE) > 0);
	}

	@Override
	public boolean isIgnored() {
		int value = this.inner_get_flags().value;
		return ((value & ScanningNode.FLAG_IGNORED) > 0);
	}

	@Override
	public int flags() {
		return this.inner_get_flags().value;
	}

}
