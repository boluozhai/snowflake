package com.boluozhai.snowflake.xgit.dao;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.impl.DefaultTreeDAOFactory;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface TreeDAO {

	public class Factory {

		public static TreeDAO create(Repository repo) {
			return DefaultTreeDAOFactory.create(repo);
		}

	}

	TreeObject getTree(ObjectId id);

	ObjectId save(TreeObject tree);

}
