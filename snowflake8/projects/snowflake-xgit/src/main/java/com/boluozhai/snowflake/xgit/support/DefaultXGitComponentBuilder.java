package com.boluozhai.snowflake.xgit.support;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.ContextBuilderFactory;
import com.boluozhai.snowflake.context.MutableContext;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.ContextWrapperFactory;
import com.boluozhai.snowflake.context.support.MutableContextBuilderFactory;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;

public class DefaultXGitComponentBuilder implements RepositoryLoader {

	private final SnowflakeContext _context;
	private RepositoryProfile _profile;
	private RepositoryOption _option;
	private URI _uri;

	public DefaultXGitComponentBuilder(SnowflakeContext context) {
		this._context = context;
	}

	public DefaultXGitComponentBuilder() {
		this._context = null;
	}

	public ComponentContext create() {
		InnerBuilder ib = new InnerBuilder();
		ib.contextParent = _context;
		ib.uri = this._uri;
		ib.option = this._option;
		ib.profile = this._profile;

		ib.loadDefaultValues();
		ib.loadComponentKeys();
		ib.createComponentBuilders();
		ib.prepareCreateContext();
		ib.initComponentBuilders();
		ib.createContext();
		ib.createComponents();
		ib.completeCreateContext();
		ib.initComponents();

		if (ib.option.check_config) {
			// TODO ...
		}

		return ib.create();
	}

	public void setProfile(RepositoryProfile pf) {
		this._profile = pf;
	}

	public void setOption(RepositoryOption option) {
		this._option = option;
	}

	public void setURI(URI uri) {
		this._uri = uri;
	}

	private class InnerBuilder {

		// property
		public RepositoryProfile profile;
		public RepositoryOption option;
		public URI uri;

		// component
		private List<String> keys;
		private Map<String, ComponentBuilder> builders;
		private Map<String, Component> components;

		// context
		private SnowflakeContext contextParent;
		private ComponentContext contextFacade;
		private MutableContext contextCore;
		private ContextBuilder contextBuilder;

		public InnerBuilder() {
		}

		public void loadDefaultValues() {

			// ComponentContextWrapper
			ContextWrapperFactory factory = profile.getComponentContext();
			if (factory == null) {
				factory = new DefaultXGitContextWrapperFactory();
				profile.setComponentContext(factory);
			}

			// option
			if (this.option == null) {
				this.option = new RepositoryOption();
			}

		}

		public ComponentContext create() {
			return this.contextFacade;
		}

		public void prepareCreateContext() {
			SnowflakeContext parent = this.contextParent;
			ContextBuilderFactory cbf = new MutableContextBuilderFactory();
			ContextBuilder builder = cbf.newBuilder(parent);
			builder.setURI(this.uri);
			this.contextBuilder = builder;

			// put ComponentBuilders to ContextBuilder
			Map<String, ComponentBuilder> tab = this.builders;
			for (String key : keys) {
				ComponentBuilder cb = tab.get(key);
				builder.setAttribute(key, cb);
			}

			// load default property
			Map<String, String> props = profile.getDefaultProperties();
			for (String key : props.keySet()) {
				String value = props.get(key);
				contextBuilder.setProperty(key, value);
			}

		}

		public void createContext() {

			// load final property
			Map<String, String> props = profile.getFinalProperties();
			for (String key : props.keySet()) {
				String value = props.get(key);
				contextBuilder.setProperty(key, value);
			}

			// create context
			ContextWrapperFactory factory = profile.getComponentContext();
			MutableContext mutable = (MutableContext) contextBuilder.create();
			this.contextCore = mutable;
			this.contextFacade = (ComponentContext) factory.wrap(mutable);
		}

		public void completeCreateContext() {
			Map<String, Component> tab = this.components;
			MutableContext cc = this.contextCore;
			for (String key : keys) {
				Component comp = tab.get(key);
				cc.setAttribute(key, comp);
			}
		}

		public void initComponents() {
			Map<String, Component> tab = this.components;
			for (String key : keys) {
				Component comp = tab.get(key);
				comp.lifecycle().onCreate();
			}
		}

		public void initComponentBuilders() {
			ContextBuilder cb = this.contextBuilder;
			Map<String, ComponentBuilder> tab = this.builders;
			for (String key : keys) {
				ComponentBuilder builder = tab.get(key);
				builder.configure(cb);
			}
		}

		public void createComponents() {
			ComponentContext cc = this.contextFacade;
			Map<String, ComponentBuilder> tab1 = this.builders;
			Map<String, Component> tab2 = new HashMap<String, Component>();
			for (String key : keys) {
				ComponentBuilder builder = tab1.get(key);
				Component comp = builder.create(cc);
				tab2.put(key, comp);
			}
			this.components = tab2;
		}

		public void createComponentBuilders() {
			Map<String, ComponentBuilderFactory> tab1 = profile.getComponents();
			Map<String, ComponentBuilder> tab2 = new HashMap<String, ComponentBuilder>();
			for (String key : keys) {
				ComponentBuilderFactory factory = tab1.get(key);
				ComponentBuilder builder = factory.newBuilder();
				tab2.put(key, builder);
			}
			this.builders = tab2;
		}

		public void loadComponentKeys() {
			final Map<String, ComponentBuilderFactory> tab;
			tab = this.profile.getComponents();
			List<String> list = new ArrayList<String>(tab.keySet());
			Collections.sort(list);
			this.keys = list;
		}
	}

	@Override
	public Repository load(OpenRepositoryParam param) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("no impl");
	}

}
