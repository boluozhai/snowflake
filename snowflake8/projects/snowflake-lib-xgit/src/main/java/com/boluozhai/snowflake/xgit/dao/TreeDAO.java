package com.boluozhai.snowflake.xgit.dao;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.impl.DefaultTreeDAOFactory;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface TreeDAO {

	public class Factory {

		public static TreeDAO create(Repository repo) {
			return DefaultTreeDAOFactory.create(repo);
		}

		public static TreeDAO create(ObjectBank bank) {
			return DefaultTreeDAOFactory.create(bank);
		}

	}

	TreeObject getTree(ObjectId id) throws IOException;

	ObjectId save(TreeObject tree) throws IOException;

}
