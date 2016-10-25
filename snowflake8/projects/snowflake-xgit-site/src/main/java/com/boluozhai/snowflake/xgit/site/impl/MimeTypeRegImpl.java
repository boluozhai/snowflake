package com.boluozhai.snowflake.xgit.site.impl;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.site.base.AbstractMimeTypeRegistrar;

public class MimeTypeRegImpl {

	public static ComponentBuilder newBuilder() {
		return new MyBuilder();
	}

	private static class MyBuilder implements ComponentBuilder {

		@Override
		public void configure(ContextBuilder cb) {
		}

		@Override
		public Component create(ComponentContext cc) {
			MyTypeReg reg = new MyTypeReg(cc);
			return reg.facade();
		}
	}

	private static class MyTypeReg extends AbstractMimeTypeRegistrar {

		public MyTypeReg(ComponentContext context) {
			super(context);
		}
	}

}
