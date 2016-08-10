package com.boluozhai.snowflake.xgit.vfs;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.boluozhai.snowflake.test.TestContext;
import com.boluozhai.snowflake.test.Tester;
import com.boluozhai.snowflake.test.Testing;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.dao.TreeDAO;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.pojo.PlainId;
import com.boluozhai.snowflake.xgit.pojo.TreeItem;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.refs.ReferenceManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;

public class TestGitObjectsRW {

	@Test
	public void test() {

		Tester tester = null;
		Testing testing = null;

		try {
			tester = Tester.Factory.newInstance();
			testing = tester.open(this);
			TestContext context = testing.context();

			File wkdir = context.getWorkingPath();
			URI uri = (new File(wkdir, "repo-example")).toURI();

			RepositoryManager rm = XGit.getRepositoryManager(context);
			Repository repo = rm.open(context, uri, null);
			URI u2 = repo.getComponentContext().getURI();
			System.out.println(this + ".repo.uri = " + u2);

			// iterate objects

			ObjectId commit_id = this.inner_get_commit_id(repo);
			CommitObject commit = this.inner_get_commit(repo, commit_id);
			System.out.format("commit %s %s\n", commit_id, commit.getBody());
			ObjectId tree_id = commit.getTree();
			this.inner_iter_tree_nodes(repo, tree_id, "", 30);

			// end

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			tester.close(testing);
		}

	}

	private void inner_iter_tree_nodes(Repository repo, ObjectId tree_id,
			String path, int depth_limit) throws IOException {

		// TODO Auto-generated method stub

		if (depth_limit < 0) {
			throw new RuntimeException("the path is too deep.");
		}

		TreeDAO dao = TreeDAO.Factory.create(repo);
		TreeObject tree = dao.getTree(tree_id);

		Map<String, TreeItem> items = tree.getItems();
		List<String> keys = new ArrayList<String>(items.keySet());
		Collections.sort(keys);

		this.inner_log_dir(path, tree_id, tree);

		for (String key : keys) {

			TreeItem it = items.get(key);
			int mode = it.getMode();
			PlainId pid = it.getId();
			ObjectId child_id = PlainId.convert(pid);

			if (mode == TreeItem.MODE.directory) {
				String path2 = path + key + '/';
				int lim2 = depth_limit - 1;
				this.inner_iter_tree_nodes(repo, child_id, path2, lim2);
			} else {
				this.inner_log_file(repo, path, it);
			}

		}

	}

	private void inner_log_file(Repository repo, String path, TreeItem it) {
		// TODO Auto-generated method stub

		System.out.format("    %s\n", it.getName());

	}

	private void inner_log_dir(String path, ObjectId tree_id, TreeObject tree) {
		// TODO Auto-generated method stub

		System.out.format("${working-dir}/%s    %s\n", path, tree_id);

	}

	private CommitObject inner_get_commit(Repository repo, ObjectId commit_id)
			throws IOException {

		ObjectBank bank = repo.context().getBean(XGitContext.component.objects,
				ObjectBank.class);

		CommitDAO dao = CommitDAO.Factory.create(bank);
		CommitObject commit = dao.getCommit(commit_id);
		return commit;
	}

	private ObjectId inner_get_commit_id(Repository repo) {
		ReferenceManager rm = repo.getComponentContext().getBean(
				XGitContext.component.refs, ReferenceManager.class);
		return rm.findTargetId(ReferenceManager.name.HEAD);
	}

}
