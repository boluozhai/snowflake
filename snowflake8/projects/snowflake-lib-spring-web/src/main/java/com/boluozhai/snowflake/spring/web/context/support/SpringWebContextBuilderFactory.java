package com.boluozhai.snowflake.spring.web.context.support;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.base.AbstractContextBuilder;
import com.boluozhai.snowflake.context.base.AbstractContextBuilderFactory;
import com.boluozhai.snowflake.context.utils.ContextBuilderConfigHelper;
import com.boluozhai.snowflake.context.utils.SnowContextUtils;
import com.boluozhai.snowflake.spring.utils.SpringContextBuilderConfigHelper;

public class SpringWebContextBuilderFactory extends
		AbstractContextBuilderFactory {

	@Override
	public ContextBuilder newBuilder() {
		return this.get_builder(null);
	}

	@Override
	public ContextBuilder newBuilder(SnowContext parent) {
		return this.get_builder(parent);
	}

	private ContextBuilder get_builder(SnowContext parent) {
		return new Builder(parent);
	}

	private class Builder extends AbstractContextBuilder {

		protected Builder(SnowContext parent) {
			super(parent);
		}

		@Override
		public SnowContext create() {

			this.load_attributes();
			this.load_parameters();
			this.load_properties();
			this.load_environments();

			return super.create();

		}

		private void load_attributes() {

			ServletContext sc = (ServletContext) this
					.getAttribute(SnowContextUtils.InitialAttributeName.webapp_servlet_context);

			WebApplicationContext ac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(sc);

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
