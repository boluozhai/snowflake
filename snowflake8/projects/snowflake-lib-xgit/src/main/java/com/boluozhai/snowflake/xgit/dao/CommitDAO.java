package com.boluozhai.snowflake.xgit.dao;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.impl.DefaultCommitDAOFactory;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface CommitDAO {

	public class Factory {

		public static CommitDAO create(Repository repo) {
			return DefaultCommitDAOFactory.create(repo);
		}

	}

	CommitObject getCommit(ObjectId id);

	ObjectId save(CommitObject commit);

}
