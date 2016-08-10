package com.boluozhai.snowflake.xgit.dao.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.TagDAO;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.TagObject;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class DefaultTagDAOFactory {

	public static TagDAO create(Repository repo) {
		return new MyDAO(repo);
	}

	private static class MyDAO implements TagDAO {

		private final ObjectBank _bank;

		public MyDAO(Repository repo) {
			ObjectBank bank = repo.getComponentContext().getBean(
					XGitContext.component.objects, ObjectBank.class);
			this._bank = bank;
		}

		@Override
		public TagObject getTag(ObjectId id) {
			GitObject obj = _bank.object(id);
			TagObject tag = new TagObject();
			try {
				CommitLikedTextObjectDAO.load(obj, tag);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return tag;
		}

		@Override
		public ObjectId save(TagObject tag) throws IOException {
			GitObject go = CommitLikedTextObjectDAO.save(tag, _bank);
			return go.id();
		}

	}

}
