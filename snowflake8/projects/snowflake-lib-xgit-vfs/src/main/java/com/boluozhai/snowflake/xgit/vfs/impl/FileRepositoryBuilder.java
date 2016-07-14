package com.boluozhai.snowflake.xgit.vfs.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boluozhai.snow.mvc.model.Component;
import com.boluozhai.snow.mvc.model.ComponentBuilder;
import com.boluozhai.snow.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snow.mvc.model.ComponentLifecycle;
import com.boluozhai.snow.vfs.MutablePathNode;
import com.boluozhai.snow.vfs.VFS;
import com.boluozhai.snow.vfs.VPath;
import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.context.support.DefaultContextBuilderFactory;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryOption;
import com.boluozhai.snowflake.xgit.support.RepositoryProfile;

public class FileRepositoryBuilder {

	private class BuildContext {

		Map<String, ComponentBuilderFactory> tab_cbf;
		Map<String, ComponentBuilder> tab_cb;
		Map<String, Component> tab_com;
		Map<String, VPath> tab_path;

		Set<String> keys;

		ContextBuilder context_builder;
		FileRepoContextAgent context_facade_agent;
		URI uri;
		SnowContext parent;
		RepositoryOption option;
		RepositoryProfile profile;

	}

	private final BuildContext context;

	public FileRepositoryBuilder(SnowContext parent, RepositoryProfile profile) {

		BuildContext context = new BuildContext();

		context.parent = parent;
		context.profile = profile;

		context.tab_cbf = profile.getComponents();
		context.tab_cb = new HashMap<String, ComponentBuilder>();
		context.tab_com = new HashMap<String, Component>();
		context.tab_path = new HashMap<String, VPath>();

		context.keys = context.tab_cbf.keySet();

		this.context = context;

	}

	public Repository create(URI uri, RepositoryOption option) {

		DefaultContextBuilderFactory context_factory = new DefaultContextBuilderFactory();

		context.context_builder = context_factory.newBuilder(context.parent);
		context.context_facade_agent = new FileRepoContextAgent();
		context.uri = uri;
		context.option = option;

		this.make_path();
		this.make_com_builder();
		this.make_com();
		this.make_com_context_core();
		this.init_com();

		ComponentContext facade = context.context_facade_agent.getFacade();
		return (Repository) facade.getBean(XGitContext.component.repository);
	}

	private void make_com_context_core() {
		SnowContext core = context.context_builder.create();
		context.context_facade_agent.setCore(core);
	}

	private void init_com() {
		for (String key : context.keys) {
			Component com = context.tab_com.get(key);
			ComponentLifecycle lc = com.lifecycle();
			if (lc != null) {
				lc.onCreate();
			}
		}
	}

	private void make_com() {
		ComponentContext facade = context.context_facade_agent.getFacade();
		ContextBuilder context_builder = context.context_builder;
		for (String key : context.keys) {
			ComponentBuilder builder = context.tab_cb.get(key);
			Component com = builder.create(facade, context_builder);
			context.tab_com.put(key, com);
			context_builder.setAttribute(key, com);
		}
	}

	private void make_path() {
		final VFS vfs = VFS.Factory.getVFS(context.parent);
		final VPath repo_path = vfs.newFile(context.uri).toPath();
		for (String key : context.keys) {
			VPath path = repo_path.child(key);
			context.tab_path.put(key, path);
		}
		context.tab_path.put(XGitContext.component.repository, repo_path);
		context.tab_path.put(XGitContext.component.workspace, repo_path.parent());
	}

	private void make_com_builder() {
		for (String key : context.keys) {
			ComponentBuilderFactory cbf = context.tab_cbf.get(key);
			ComponentBuilder cb = cbf.newBuilder();
			context.tab_cb.put(key, cb);
			VPath path = context.tab_path.get(key);
			MutablePathNode node = (MutablePathNode) cb;
			node.setPath(path);
		}
	}

}
