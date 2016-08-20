package com.boluozhai.snowflake.test.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.context.base.AbstractContextBuilderFactory;
import com.boluozhai.snowflake.context.utils.ContextBuilderConfigHelper;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.spring.utils.SpringContextBuilderConfigHelper;

public class TestingContextBuilderFactory extends AbstractContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.get_builder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowflakeContext parent) {
		return this.get_builder(parent);
	}

	private ContextBuilder get_builder(SnowflakeContext parent) {
		return new Builder(parent);
	}

	private class Builder extends AbstractContextBuilder {

		protected Builder(SnowflakeContext parent) {
			super(parent);
		}

		@Override
		public SnowflakeContext create() {

			this.load_attributes();
			this.load_parameters();
			this.load_properties();
			this.load_environments();

			return super.create();
		}

		private void load_attributes() {

			Object target = this
					.getAttribute(SnowContextUtils.InitialAttributeName.junit_target_object);
			Class<?> type = target.getClass();
			String path = type.getSimpleName() + ".xml";

			ApplicationContext ac = new ClassPathXmlApplicationContext(path,
					type);
			SpringContextBuilderConfigHelper.config(this, ac);

		}

		private void load_parameters() {
			// NOP
		}

		private void load_properties() {
			ContextBuilderConfigHelper.config_system_properties(this);
		}

		private void load_environments() {
			ContextBuilderConfigHelper.config_system_environments(this);
		}

	}

}
