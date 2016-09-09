package com.boluozhai.snowflake.xgit.vfs.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;

public class FileHashAlgorithmFactory implements ComponentBuilderFactory {

	@Override
	public ComponentBuilder newBuilder() {
		return new Builder();
	}

	private static class Builder implements ComponentBuilder {

		@Override
		public Component create(ComponentContext cc) {
			return new Com(cc);
		}

		@Override
		public void configure(ContextBuilder cb) {
			// NOP
		}
	}

	private static class Com implements Component, HashAlgorithmProvider {

		private final ComponentContext context;
		private String alg;

		public Com(ComponentContext cc) {
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
		public String algorithm() {
			return this.alg;
		}

		@Override
		public MessageDigest getMessageDigest() {
			try {
				return MessageDigest.getInstance(alg);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private static class Life implements ComponentLifecycle {

		private final Com com;

		public Life(Com c) {
			this.com = c;
		}

		@Override
		public void onCreate() {
			com.alg = com.context.getProperty("xgit.hashalgorithm");
		}
	}

}
