package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.HashId;
import com.boluozhai.snowflake.xgit.vfs.HashPathMapper;

public class FileHashPathMapperFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder implements ComponentBuilder {

		@Override
		public Component create(ComponentContext cc, ContextBuilder cb) {
			return new Com(cc, cb);
		}
	}

	private static class Com implements Component, HashPathMapper {

		private final ComponentContext context;
		public String hash_path_pattern;

		public Com(ComponentContext cc, ContextBuilder cb) {
			this.context = cc;
		}

		@Override
		public ComponentContext getComponentContext() {
			return this.context;
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return new Life(this);
		}

		@Override
		public VFile getHashPath(VFile base, HashId hash) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public VFile getHashPath(VFile base, HashId hash, String suffix) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class Life implements ComponentLifecycle {

		private final Com com;

		public Life(Com c) {
			this.com = c;
		}

		@Override
		public void onCreate() {

			String pattern = com.context.getProperty("xgit.hashpathpattern");
			com.hash_path_pattern = pattern;

		}
	}

}
