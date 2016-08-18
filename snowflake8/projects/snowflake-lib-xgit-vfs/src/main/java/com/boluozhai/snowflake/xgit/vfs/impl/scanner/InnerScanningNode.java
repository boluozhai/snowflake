package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VPath;
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
	public boolean isIgnored() {
		return this.getIgnore().isIgnored();
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
		ScanningNode node = this;
		VFile file = this.getFile().child(".gitignore");
		return InnerGitIgnore.open(node, file);
	}

}
