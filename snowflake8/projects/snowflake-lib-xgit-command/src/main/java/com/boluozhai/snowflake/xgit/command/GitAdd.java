package com.boluozhai.snowflake.xgit.command;

import java.net.URI;
import java.util.Arrays;

import com.boluozhai.snowflake.cli.CLICommandHandler;
import com.boluozhai.snowflake.context.SnowContext;
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

public class GitAdd implements CLICommandHandler {

	@Override
	public void process(SnowContext context, String command) {

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
		FileScanner scanner = fsb.create(works, add_path);

		// scan
		MyScanContext sc = new MyScanContext(scanner);
		sc.scan();

	}

	private class MyScanContext {

		private final FileScanner scanner;

		public MyScanContext(FileScanner scanner) {
			this.scanner = scanner;
		}

		public void scan() {
			ScanningNode node = scanner.getBaseNode();
			this.scan(node, 128);
		}

		private void scan(final ScanningNode node, final int depth_limit) {

			final VFile file = node.getFile();

			if (depth_limit < 0) {
				String msg = "the path is too deep: " + file;
				throw new RuntimeException(msg);
			}

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
			// TODO Auto-generated method stub

			System.out.println("onFile: " + node.getFile());

		}

		private void onDirEnd(ScanningNode node) {
			// TODO Auto-generated method stub

		}

		private void onDirBegin(ScanningNode node) {
			// TODO Auto-generated method stub

		}

	}

}
