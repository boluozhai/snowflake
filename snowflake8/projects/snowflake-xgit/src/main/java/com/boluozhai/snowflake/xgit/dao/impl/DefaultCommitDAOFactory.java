package com.boluozhai.snowflake.xgit.dao.impl;

import java.io.IOException;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.pojo.CommitSectionObject;

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
			String type = obj.type();
			CommitObject commit = null;

			if (type.equals(GitObject.TYPE.commit)) {
				commit = new CommitObject();
			} else {
				commit = new CommitSectionObject();
			}

			try {
				CommitLikedTextObjectDAO.load(obj, commit);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			return commit;
		}

		@Override
		public ObjectId saveCommit(CommitObject commit) throws IOException {

			boolean sec = (commit instanceof CommitSectionObject);
			String type = sec ? GitObject.TYPE.commit_section
					: GitObject.TYPE.commit;

			commit.setType(type);
			GitObject go = CommitLikedTextObjectDAO.save(commit, _bank);
			return go.id();

		}

	}

}
