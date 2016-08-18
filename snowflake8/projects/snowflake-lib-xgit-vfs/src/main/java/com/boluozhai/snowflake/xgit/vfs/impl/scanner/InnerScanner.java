package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScanner;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;
import com.boluozhai.snowflake.xgit.vfs.scanner.UserDataFactory;

final class InnerScanner implements FileScanner {

	private UserDataFactory _user_data_factory;
	private ScanningNode _base_node;
	private FileWorkspace _workspace;
	private VPath _base_path;

	private InnerScanner() {
	}

	public static InnerScanner create(FileWorkspace works, String[] path,
			UserDataFactory ud_factory) {

		// default value
		if (ud_factory == null) {
			ud_factory = new DefaultUserDataFactory();
		}

		// create root scanner
		final InnerScanner scanner = new InnerScanner();
		final VPath root_path = works.getFile().toPath();
		final InnerScanningNode root_node = new InnerScanningNode(scanner,
				root_path, null);
		scanner._workspace = works;
		scanner._user_data_factory = ud_factory;
		scanner._base_path = root_path;
		scanner._base_node = root_node;

		// make path offset
		ScanningNode node = root_node;
		for (String part : path) {
			node = node.child(part);
		}
		scanner._base_path = node.getPath();
		scanner._base_node = node;

		return scanner;
	}

	@Override
	public VPath getBasePath() {
		return this._base_path;
	}

	@Override
	public FileWorkspace getWorkspace() {
		return this._workspace;
	}

	@Override
	public ScanningNode getBaseNode() {
		return this._base_node;
	}

	@Override
	public UserDataFactory getUserDataFactory() {
		return this._user_data_factory;
	}

}
