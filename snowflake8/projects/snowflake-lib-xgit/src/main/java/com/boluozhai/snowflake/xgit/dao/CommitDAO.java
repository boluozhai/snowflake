package com.boluozhai.snowflake.xgit.dao;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.impl.DefaultCommitDAOFactory;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;

public interface CommitDAO {

	public class Factory {

		public static CommitDAO create(ObjectBank bank) {
			return DefaultCommitDAOFactory.create(bank);
		}

	}

	CommitObject getCommit(ObjectId id) throws IOException;

	ObjectId save(CommitObject commit) throws IOException;

}
