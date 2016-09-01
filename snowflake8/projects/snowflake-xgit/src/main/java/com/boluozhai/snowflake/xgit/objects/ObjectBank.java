package com.boluozhai.snowflake.xgit.objects;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface ObjectBank extends XGitComponent {

	GitObject object(ObjectId id);

	GitObjectBuilder newBuilder(String type, long length);

	class Factory {

		public static ObjectBank getBank(Repository repo) {
			XGitContext context = repo.context();
			String key = XGitContext.component.objects;
			return context.getBean(key, ObjectBank.class);
		}

	}

}
