package com.boluozhai.snowflake.xgit.vfs.impl.scanner;

import java.net.URI;
import java.util.ArrayList;

import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScanner;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilder;
import com.boluozhai.snowflake.xgit.vfs.scanner.UserDataFactory;

final class InnerFileScannerBuilder implements FileScannerBuilder {

	private final SnowContext _context;
	private UserDataFactory _user_data_factory;

	public InnerFileScannerBuilder(SnowContext context) {
		this._context = context;
	}

	@Override
	public SnowContext context() {
		return this._context;
	}

	@Override
	public FileScanner create(VFile path) {
		URI uri = path.toURI();
		RepositoryManager rm = XGit.getRepositoryManager(_context);
		Repository repo = rm.open(_context, uri, null);
		FileWorkspace works = repo.getComponentContext().getBean(
				XGitContext.component.workspace, FileWorkspace.class);
		return this.create(works);
	}

	@Override
	public FileScanner create(FileWorkspace workspace) {
		VFile path = workspace.getFile();
		return this.create(workspace, path);
	}

	@Override
	public FileScanner create(FileWorkspace workspace, VFile path) {

		final VFile wkdir = workspace.getFile();
		final String f1 = wkdir.toURI().toString();
		final String f2 = path.toURI().toString();

		// check parent-child
		if (!f2.startsWith(f1)) {
			String msg = "the path [%s] is NOT a child of the workspace [%s].\n";
			msg = String.format(msg, f2, f1);
			throw new RuntimeException(msg);
		}

		// path to Array
		String offset = f2.substring(f1.length());
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (char ch : offset.toCharArray()) {
			if (ch == '\\' || ch == '/') {
				Helper.flush_path_name_to_list(list, sb);
			} else {
				sb.append(ch);
			}
		}
		Helper.flush_path_name_to_list(list, sb);
		String[] array = list.toArray(new String[list.size()]);

		// create
		return InnerScanner.create(workspace, array, this._user_data_factory);
	}

	private static class Helper {

		public static void flush_path_name_to_list(ArrayList<String> list,
				StringBuilder sb) {

			if (sb.length() > 0) {
				list.add(sb.toString());
				sb.setLength(0);
			}

		}
	}

	@Override
	public UserDataFactory getUserDataFactory() {
		return this._user_data_factory;
	}

	@Override
	public void setUserDataFactory(UserDataFactory factory) {
		this._user_data_factory = factory;
	}

}
