package com.boluozhai.snowflake.xgit.vfs.impl.refs;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.refs.HrefManager;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.refs.RefPointer;
import com.boluozhai.snowflake.xgit.refs.RefPointerManager;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponentBuilder;

final class FileHreferManagerImpl implements HrefManager {

	public static ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder extends FileXGitComponentBuilder {

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {
			return new FileHreferManagerImpl(cc);
		}

	}

	private class Life implements ComponentLifecycle {

		@Override
		public void onCreate() {
			FileHreferManagerImpl.this.inner.onCreate();
		}

	}

	private static class Inner {

		private final ComponentContext context;
		private RefPointerManager refptrs;
		private RefManager refs;

		public Inner(ComponentContext cc) {
			this.context = cc;
		}

		public void onCreate() {

			this.refs = (RefManager) context
					.getAttribute(XGitContext.component.refs);

			this.refptrs = (RefPointerManager) context
					.getAttribute(XGitContext.component.refptrs);

		}

		public Ref find_ref_by_refname(String refname) {
			return refs.getReference(refname);
		}
	}

	private final Inner inner;

	private FileHreferManagerImpl(ComponentContext cc) {
		this.inner = new Inner(cc);
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
	public Ref findRef(String name) {

		if (name == null) {
			return null;

		} else if (name.indexOf('\\') >= 0) {
			name = name.replace('\\', '/');
			return inner.refs.getReference(name);

		} else if (name.indexOf('/') >= 0) {
			return inner.refs.getReference(name);

		} else {
			RefPointer ptr = inner.refptrs.getPointer(name);
			String name2 = ptr.getRefname();
			return inner.find_ref_by_refname(name2);
		}

	}

	@Override
	public ObjectId findId(String name) {
		Ref ref = this.findRef(name);
		if (ref == null) {
			return null;
		} else {
			return ref.getId();
		}
	}

}
