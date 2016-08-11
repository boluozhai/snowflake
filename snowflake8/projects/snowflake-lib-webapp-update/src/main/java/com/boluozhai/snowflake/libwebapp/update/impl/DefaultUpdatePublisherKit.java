package com.boluozhai.snowflake.libwebapp.update.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.appdata.AppData;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.libwebapp.pojo.WebappInfo;
import com.boluozhai.snowflake.libwebapp.pojo.WebappPOM;
import com.boluozhai.snowflake.libwebapp.pojo.WebappSet;
import com.boluozhai.snowflake.libwebapp.update.UpdatePublisherKit;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.refs.Reference;
import com.boluozhai.snowflake.xgit.refs.ReferenceManager;
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

	public DefaultUpdatePublisherKit(SnowContext context) {
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
			ReferenceManager refs = repo.context().getBean(
					XGitContext.component.refs, ReferenceManager.class);
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
			List<WebappInfo> list = new ArrayList<>();
			Collections.sort(keys);
			for (String key : keys) {
				list.add(apps.get(key));
			}

			Reference ref = refs.getReference(Refs.add);
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
			ReferenceManager refs = repo.context().getBean(
					XGitContext.component.refs, ReferenceManager.class);
			ObjectBank bank = repo.context().getBean(
					XGitContext.component.objects, ObjectBank.class);

			// load commit
			Reference ref_1 = refs.getReference(Refs.add);
			Reference ref_2 = refs.getReference(Refs.commit);
			CommitDAO commit_dao = CommitDAO.Factory.create(bank);
			CommitObject commit = commit_dao.getCommit(ref_1.getTargetId());

			// modify commit
			commit.setBody("the update-log");
			commit.setHeaderValue("version", "v" + System.currentTimeMillis());

			// save commit
			ObjectId commit2_id = commit_dao.save(commit);
			ref_2.setTargetId(commit2_id);

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
