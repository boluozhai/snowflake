package com.boluozhai.snowflake.xgit.vfs.support;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.vfs.TemporaryFileManager;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

public class TemporaryFileManagerFactory implements ComponentBuilderFactory {

	private final static IndexGen index_gen;

	static {
		index_gen = new IndexGen();
	}

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends FileXGitComponentBuilder {

		@Override
		public Component create(ComponentContext cc) {
			return new TempFileManImpl(this, cc);
		}

		@Override
		public void configure(ContextBuilder cb) {
			// NOP
		}

	}

	private static class TempFileManImpl implements TemporaryFileManager {

		private final ComponentContext _com_context;
		private final VFile _file;

		public TempFileManImpl(Builder builder, ComponentContext cc) {

			this._com_context = cc;
			this._file = builder.getPath().file();

		}

		@Override
		public VFile newTemporaryFile() {
			return this.newTemporaryFile(null, null);
		}

		@Override
		public VFile newTemporaryFile(String prefix, String suffix) {
			StringBuilder sb = new StringBuilder();
			if (prefix != null) {
				sb.append(prefix);
			}
			index_gen.next_file_name(sb);
			if (suffix != null) {
				sb.append(suffix);
			}
			return _file.child(sb.toString());
		}

		@Override
		public VFile getFile() {
			return this._file;
		}

		@Override
		public ComponentLifecycle lifecycle() {
			return new Life();
		}

		@Override
		public ComponentContext getComponentContext() {
			return this._com_context;
		}

	}

	private static class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}

	}

	private static class IndexGen {

		private final long _birthday;
		private long _index_count;

		public IndexGen() {
			this._birthday = System.currentTimeMillis();
		}

		public void next_file_name(StringBuilder sb) {

			long birth = this._birthday;
			long now = System.currentTimeMillis();
			long index = this.nextIndex();

			sb.append(birth).append("-");
			sb.append(index).append("-");
			sb.append(now);

		}

		public synchronized long nextIndex() {
			return (this._index_count++);
		}

	}

}
