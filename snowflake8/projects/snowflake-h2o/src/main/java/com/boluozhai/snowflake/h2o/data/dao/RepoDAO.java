package com.boluozhai.snowflake.h2o.data.dao;

import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.RepoDTM;

public class RepoDAO {

	private final DataClient client;

	public RepoDAO(DataClient dc) {
		this.client = dc;
	}

	public RepoItem findRepo(String user_id, String repo_id) {
		RepoDTM model = client.get(user_id, RepoDTM.class);
		if (model == null) {
			return null;
		}
		RepoItem item = model.getTable().get(repo_id);
		return item;
	}

	public RepoDTM getRepoModel(String uid) {
		return client.get(uid, RepoDTM.class);
	}

}
