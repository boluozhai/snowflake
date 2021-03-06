package com.boluozhai.snowflake.xgit.dao;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.impl.DefaultTagDAOFactory;
import com.boluozhai.snowflake.xgit.pojo.TagObject;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface TagDAO {

	public class Factory {

		public static TagDAO create(Repository repo) {
			return DefaultTagDAOFactory.create(repo);
		}

	}

	TagObject getTag(ObjectId id) throws IOException;

	ObjectId save(TagObject tree) throws IOException;

}
