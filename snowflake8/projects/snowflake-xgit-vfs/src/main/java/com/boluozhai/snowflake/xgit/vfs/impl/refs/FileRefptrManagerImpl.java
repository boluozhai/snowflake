package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.refs.RefPointer;
import com.boluozhai.snowflake.xgit.refs.RefPointerManager;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

final class FileRefptrManagerImpl implements RefPointerManager {

	public static ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends FileXGitComponentBuilder {

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {
			VPath path = this.getPath();
			return new FileRefptrManagerImpl(cc, path);
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
		}

	}

	private static class Inner {

		private final ComponentContext context;
		private final VPath path;

		public Inner(ComponentContext context, VPath path) {
			this.context = context;
			this.path = path;
		}

		public String normal_name(String name) {
			return name.toUpperCase().trim();
		}

		public void check_name(String name) {
			int i1 = name.indexOf('/');
			int i2 = name.indexOf('\\');
			if (i1 < 0 && i2 < 0) {
				// OK
			} else {
				throw new RuntimeException("bad ref-ptr name : " + name);
			}
		}

		public RefPointer get_ptr(String name) {
			VPath node = this.path.parent().child(name);
			return new RefPointerImpl(name, node);
		}

	}

	private final Inner inner;

	private FileRefptrManagerImpl(ComponentContext context, VPath path) {
		this.inner = new Inner(context, path);
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new Life();
	}

	@Override
	public ComponentContext getComponentContext() {
		return this.inner.context;
	}

	@Override
	public RefPointer getPointer(String name) {
		name = inner.normal_name(name);
		inner.check_name(name);
		return inner.get_ptr(name);
	}

}
