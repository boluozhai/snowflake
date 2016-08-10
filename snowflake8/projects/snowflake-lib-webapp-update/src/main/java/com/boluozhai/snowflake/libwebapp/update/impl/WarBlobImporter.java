package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.io.VFSIO;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.dao.TreeDAO;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.pojo.PlainId;
import com.boluozhai.snowflake.xgit.pojo.TreeItem;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;

public class WarBlobImporter {

	private final VFS _vfs;
	private final FileRepository _repo;
	private final FileObjectBank _bank;

	private final Map<String, WebappInfo> _table;

	public WarBlobImporter(FileRepository repo) {
		this._repo = repo;
		this._vfs = repo.getFile().vfs();
		this._bank = repo.getComponentContext().getBean(
				XGitContext.component.objects, FileObjectBank.class);

		this._table = new HashMap<String, WebappInfo>();

	}

	public void doImport(WebappInfo info) {

		OutputStream out = null;
		InputStream in = null;
		GitObjectBuilder builder = null;

		try {

			// input

			String uri = info.getWarFileURI();
			VFile file = _vfs.newFile(URI.create(uri));

			FileRepositoryContext context = _repo.context();
			VFSIO io = VFSIO.Agent.getInstance(context);
			in = io.input(file);

			// output

			String type = GitObject.TYPE.blob;
			long length = file.length();
			builder = _bank.newBuilder(type, length);
			out = builder.getOutputStream();

			// pump

			IOTools.pump(in, out);

			// done

			GitObject obj = builder.create();
			ObjectId id = obj.id();
			info.setWarFileHash(id.toString());

			String key = info.getName();
			WebappInfo info0 = _table.get(key);
			if (info0 != null) {
				this.inner_throw_info_exists(info0, info);
			} else {
				_table.put(key, info);
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			IOTools.close(in);
			IOTools.close(out);

		}

	}

	private void inner_throw_info_exists(WebappInfo info0, WebappInfo info1) {

		String name = info0.getName();
		String uri0 = info0.getWarFileURI();
		String uri1 = info1.getWarFileURI();

		String msg = "the 2 war files have same name '%s' : file1=%s ; file2=%s";
		msg = String.format(msg, name, uri0, uri1);
		throw new RuntimeException(msg);

	}

	public ObjectId makeCommit(String branch_name) throws IOException {

		FileRepository repo = this._repo;
		ObjectBank bank = repo.context().getBean(XGitContext.component.objects,
				ObjectBank.class);

		// make tree
		TreeObject tree = new TreeObject();
		this.inner_make_tree(tree);
		TreeDAO tree_dao = TreeDAO.Factory.create(repo);
		ObjectId tree_id = tree_dao.save(tree);

		// make commit
		CommitObject commit = new CommitObject();
		this.inner_make_commit(commit, tree_id, tree);
		CommitDAO commit_dao = CommitDAO.Factory.create(bank);
		ObjectId commit_id = commit_dao.save(commit);

		return commit_id;
	}

	private void inner_make_tree(TreeObject tree) {

		Map<String, TreeItem> items = new HashMap<String, TreeItem>();
		Map<String, WebappInfo> tab = this._table;
		Set<String> keys = tab.keySet();

		for (String key : keys) {
			WebappInfo info = tab.get(key);
			String k2 = info.getName();
			TreeItem item = new TreeItem();
			PlainId pid = new PlainId(info.getWarFileHash());
			item.setMode(TreeItem.MODE.normal);
			item.setId(pid);
			items.put(k2, item);
		}

		tree.setItems(items);

	}

	private void inner_make_commit(CommitObject commit, ObjectId tree_id,
			TreeObject tree) {

		// TODO Auto-generated method stub

		commit.setTree(tree_id);

	}

}
