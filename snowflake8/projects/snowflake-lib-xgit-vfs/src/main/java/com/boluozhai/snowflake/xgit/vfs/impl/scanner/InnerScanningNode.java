package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScanner;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;

public class InnerScanningNode implements ScanningNode {

	private final FileScanner _scanner;
	private final VPath _path;
	private final String _name;
	private Object _user_data;

	public InnerScanningNode(FileScanner scanner, VPath path) {
		this._scanner = scanner;
		this._path = path;
		this._name = path.file().getName();
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
		return new InnerScanningNode(_scanner, child_path);
	}

}
