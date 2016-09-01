package com.boluozhai.snowflake.xgit.command;

import java.net.URI;
import java.util.Arrays;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScanner;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilder;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerBuilderFactory;
import com.boluozhai.snowflake.xgit.vfs.scanner.FileScannerUtils;
import com.boluozhai.snowflake.xgit.vfs.scanner.ScanningNode;
import com.boluozhai.snowflake.xgit.vfs.scanner.UserDataFactory;

public class XGitScan implements CLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		// repository
		RepositoryAgent repo_agent = RepositoryAgent.Factory.get(context);
		Repository repo = repo_agent.getRepository(context);
		XGitContext xgit_context = repo.context();

		// workspace
		FileWorkspace works = xgit_context.getBean(
				XGitContext.component.workspace, FileWorkspace.class);
		VFile wk_dir = works.getFile();
		VFS vfs = wk_dir.vfs();

		// location
		CurrentLocation cur_loc = CurrentLocation.Factory.get(context);
		URI location = cur_loc.getLocation(context);
		VFile add_path = vfs.newFile(location);

		// scanner
		FileScannerBuilderFactory fsb_factory = FileScannerUtils.getFactory();
		FileScannerBuilder fsb = fsb_factory.newBuilder(xgit_context);
		fsb.setUserDataFactory(new MyUserDataFactory());
		FileScanner scanner = fsb.create(works, add_path);

		// scan
		MyScanContext sc = new MyScanContext(scanner);
		sc.scan();

	}

	private static class MyScanContext {

		private final FileScanner scanner;
		private int _base_path_length;

		public MyScanContext(FileScanner scanner) {
			this.scanner = scanner;
		}

		public void scan() {
			ScanningNode node = scanner.getBaseNode();
			this._base_path_length = scanner.getBasePath().file()
					.getAbsolutePath().length();

			this.log_base_info(node);

			this.scan(node, 128);
		}

		private void log_base_info(ScanningNode node) {

			ScanningNode p = node;
			for (; p != null; p = p.parent()) {
				VFile path = p.getFile();
				System.out.println("base: " + path);
			}

		}

		private void scan(final ScanningNode node, final int depth_limit) {

			final VFile file = node.getFile();

			if (depth_limit < 0) {
				String msg = "the path is too deep: " + file;
				throw new RuntimeException(msg);
			}

			// if (node.isIgnored()) {
			// System.out.println("ignore " + node.getName());
			// return;
			// }

			if (file.isDirectory()) {
				this.onDirBegin(node);
				String[] list = file.list();
				Arrays.sort(list);
				for (String name : list) {
					ScanningNode ch = node.child(name);
					this.scan(ch, depth_limit - 1);
				}
				this.onDirEnd(node);
			} else {
				this.onFile(node);
			}

		}

		private void onFile(ScanningNode node) {
			this.on_node(node);
		}

		private void onDirEnd(ScanningNode node) {
			this.on_node(node);
		}

		private void onDirBegin(ScanningNode node) {
			this.on_node(node);
		}

		private void on_node(ScanningNode node) {

			char ch_ignore = node.isIgnored() ? 'I' : '_';
			char ch_works = node.isInWorkspace() ? 'W' : '_';
			char ch_repo = node.isInRepository() ? 'R' : '_';

			String path = this.get_short_path(node);

			String msg = String.format("%c%c%c %s", ch_ignore, ch_repo,
					ch_works, path);

			System.out.println(msg);

		}

		private String get_short_path(ScanningNode node) {
			final int bpl = this._base_path_length;
			String path = node.getFile().getAbsolutePath();
			if (path.length() > bpl) {
				return "{base}" + path.substring(bpl);
			} else {
				return path;
			}
		}

	}

	private static class MyUserData {

		private final ScanningNode _node;

		public MyUserData(ScanningNode node) {
			this._node = node;
		}

	}

	private static class MyUserDataFactory implements UserDataFactory {

		@Override
		public Object createUserData(ScanningNode node) {
			return new MyUserData(node);
		}
	}

}
