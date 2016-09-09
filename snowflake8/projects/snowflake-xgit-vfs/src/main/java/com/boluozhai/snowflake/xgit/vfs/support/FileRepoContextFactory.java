package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.ContextWrapper;
import com.boluozhai.snowflake.context.support.ContextWrapperFactory;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.Element;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;

public class FileRepoContextFactory implements ContextWrapperFactory {

	@Override
	public SnowflakeContext wrap(SnowflakeContext context) {
		return new MyWrapper(context);
	}

	private static class MyWrapper extends ContextWrapper implements
			FileRepositoryContext {

		public MyWrapper(SnowflakeContext inner) {
			super(inner);
		}

		@Override
		public Repository repository() {
			Class<Repository> type = Repository.class;
			return this.getBean(XGitContext.component.repository, type);
		}

		@Override
		public Component getRootComponent() {
			return this.repository();
		}

		@Override
		public Element getElement(String key) {
			return (Element) this.getBean(key);
		}

		@Override
		public VFile getFile() {
			FileRepository repo = (FileRepository) this.repository();
			return repo.getFile();
		}
	}

}
