package com.boluozhai.snowflake.xgit.dao.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;

public class DefaultCommitDAOFactory {

	public static CommitDAO create(ObjectBank bank) {
		return new MyDAO(bank);
	}

	private static class MyDAO implements CommitDAO {

		private final ObjectBank _bank;

		public MyDAO(ObjectBank bank) {
			this._bank = bank;
		}

		@Override
		public CommitObject getCommit(ObjectId id) {
			GitObject obj = _bank.object(id);
			CommitObject commit = new CommitObject();

			try {
				CommitLikedTextObjectDAO.load(obj, commit);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			return commit;
		}

		@Override
		public ObjectId save(CommitObject commit) throws IOException {
			GitObject go = CommitLikedTextObjectDAO.save(commit, _bank);
			return go.id();
		}
	}

}
