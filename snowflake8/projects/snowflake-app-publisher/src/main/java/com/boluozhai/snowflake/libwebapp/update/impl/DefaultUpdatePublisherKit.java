package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.pojo.WebappPOM;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.libwebapp.update.UpdatePublisherKit;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.pojo.Operator;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;

public class DefaultUpdatePublisherKit extends DefaultUpdateKit implements
		UpdatePublisherKit {

	interface Refs {

		String add = "refs/publisher/add";
		String commit = "refs/publisher/commit";

	}

	interface PKey {

		String m2dir = "snowflake_update_m2_dir";
		String pom_list = "snowflake_update_pom_list_file";

	}

	public DefaultUpdatePublisherKit(SnowflakeContext context) {
		super(context);
	}

	@Override
	public WebappSet loadWebappPomList() {
		AppData appdata = this.getAppData();
		String pom_list_path = appdata.getProperty(PKey.pom_list);
		File pom_list_file = new File(pom_list_path);
		PomListFileLoader loader = new PomListFileLoader();
		return loader.load(pom_list_file);
	}

	@Override
	public ObjectId addWebappsToRepository(WebappSet webapps)
			throws IOException {
		FileRepository repo = this.getRepository();
		WarBlobImporter impo = new WarBlobImporter(repo);
		Map<String, WebappInfo> table = webapps.getApps();
		for (WebappInfo info : table.values()) {
			impo.doImport(info);
		}
		return impo.makeCommit("debug");
	}

	@Override
	public void locateWarFiles(WebappSet webapps) {

		AppData appdata = this.getAppData();
		String m2_path = appdata.getProperty(PKey.m2dir);
		M2RepoManager m2_repo = new M2RepoManager(new File(m2_path));

		Map<String, WebappInfo> table = webapps.getApps();
		for (WebappInfo item : table.values()) {
			WebappPOM pom = item.getPom();
			File war = m2_repo.locateWarFile(pom);
			URI uri = war.toURI();
			item.setWarFileURI(uri.toString());
		}

	}

	@Override
	public void add() {
		Exception error = null;
		try {

			FileRepository repo = this.getRepository();
			RefManager refs = repo.context().getBean(
					XGitContext.component.refs, RefManager.class);
			ObjectBank bank = repo.context().getBean(
					XGitContext.component.objects, ObjectBank.class);

			WebappSet webapps = this.loadWebappPomList();
			this.locateWarFiles(webapps);
			ObjectId commit_id = this.addWebappsToRepository(webapps);
			CommitDAO commit_dao = CommitDAO.Factory.create(bank);
			CommitObject commit = commit_dao.getCommit(commit_id);
			ObjectId tree_id = commit.getTree();

			// apps
			Map<String, WebappInfo> apps = webapps.getApps();
			List<String> keys = new ArrayList<String>(apps.keySet());
			List<WebappInfo> list = new ArrayList<WebappInfo>();
			Collections.sort(keys);
			for (String key : keys) {
				list.add(apps.get(key));
			}

			Ref ref = refs.getReference(Refs.add);
			ref.setTargetId(commit_id);

			System.out.println("add as tree:" + tree_id);

			return;

		} catch (IOException e) {
			error = e;
		} finally {
			// NOP
		}
		throw new RuntimeException(error);
	}

	@Override
	public void commit() {

		try {

			FileRepository repo = this.getRepository();
			RefManager refs = repo.context().getBean(
					XGitContext.component.refs, RefManager.class);
			ObjectBank bank = repo.context().getBean(
					XGitContext.component.objects, ObjectBank.class);
			CommitDAO commit_dao = CommitDAO.Factory.create(bank);

			// load commit
			final Ref ref_1 = refs.getReference(Refs.add);
			final Ref ref_2 = refs.getReference(Refs.commit);

			final ObjectId commit1_id = ref_1.getTargetId();
			final ObjectId commit2_id = ref_2.getTargetId();

			final CommitObject commit1 = commit_dao.getCommit(commit1_id);
			final CommitObject commit2 = commit_dao.getCommit(commit2_id);

			final ObjectId tree1_id = commit1.getTree();
			final ObjectId tree2_id = commit2.getTree();

			if (tree1_id.equals(tree2_id)) {
				System.out.println("nothing changed.");
				return;
			}

			// new commit

			final Operator committer = new Operator();
			final CommitObject commit3 = new CommitObject();

			committer.setMail("N/A");
			committer.setName(this.getClass().getName());
			committer.setTime(System.currentTimeMillis());
			committer.setZone(0);

			String message = "update-log";
			String version = "v" + committer.getTime();

			commit3.setHeaderValue("version", version);
			commit3.setBody(message);
			commit3.setAuthor(commit1.getAuthor());
			commit3.setTree(commit1.getTree());
			commit3.setCommitter(committer);
			commit3.addParent(commit2_id);

			// save commit
			ObjectId commit3_id = commit_dao.saveCommit(commit3);
			ref_2.setTargetId(commit3_id);

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
		}

	}

	@Override
	public void push() {
		// TODO Auto-generated method stub

	}

}
