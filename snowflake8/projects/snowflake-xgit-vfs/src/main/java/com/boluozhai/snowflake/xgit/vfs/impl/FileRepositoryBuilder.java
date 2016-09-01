package com.boluozhai.snowflake.xgit.vfs.impl;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.boluozhai.snowflake.context.ContextBuilder;
import com.boluozhai.snowflake.context.MutableProperties;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.support.DefaultContextBuilderFactory;
import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.mvc.model.ComponentBuilder;
import com.boluozhai.snowflake.mvc.model.ComponentBuilderFactory;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.MutablePathNode;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VPath;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.config.Config;
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
		SnowflakeContext parent;
		RepositoryOption option;
		RepositoryProfile profile;

	}

	private final BuildContext context;

	public FileRepositoryBuilder(SnowflakeContext parent, RepositoryProfile profile) {

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

		if (option == null) {
			option = new RepositoryOption();
		}

		DefaultContextBuilderFactory context_factory = new DefaultContextBuilderFactory();

		context.context_builder = context_factory.newBuilder(context.parent);
		context.context_facade_agent = new FileRepoContextAgent();
		context.uri = uri;
		context.option = option;
		context.context_builder.setURI(uri);

		// set the temporary context core
		context.context_facade_agent.setCore(context.parent);

		this.make_path();
		this.make_com_builder();
		this.make_com();

		this.load_default_properties();
		this.load_config();
		this.load_final_properties();
		this.make_com_context_core();

		ComponentContext facade = context.context_facade_agent.getFacade();
		if (option.check_config) {
			this.check_config(facade);
		}
		this.init_com();

		return (Repository) facade.getBean(XGitContext.component.repository);
	}

	private void load_final_properties() {

		ContextBuilder builder = context.context_builder;
		RepositoryProfile prof = context.profile;
		Map<String, String> src = prof.getFinalProperties();
		put_values_in_a2b(src, builder);

	}

	private void load_default_properties() {

		ContextBuilder builder = context.context_builder;
		RepositoryProfile prof = context.profile;
		Map<String, String> src = prof.getDefaultProperties();
		put_values_in_a2b(src, builder);

	}

	private void check_config(ComponentContext facade) {

		String k1 = Config.core.repositoryformatversion;

		String repo_fmt_ver = facade.getProperty(k1, null);

		if ("0".equals(repo_fmt_ver)) {
			// ok
		} else {
			String msg = "bad config[" + k1 + "] value: " + repo_fmt_ver;
			throw new RuntimeException(msg);
		}

	}

	private void load_config() {
		try {
			Config config = (Config) this.context.tab_com
					.get(XGitContext.component.config);
			config.load();
			// inject values to context
			ContextBuilder builder = context.context_builder;
			String[] keys = config.getPropertyNames();
			for (String key : keys) {
				String value = config.getProperty(key);
				builder.setProperty(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void make_com_context_core() {
		SnowflakeContext core = context.context_builder.create();
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
		context.tab_path.put(XGitContext.component.workspace,
				repo_path.parent());
	}

	private void make_com_builder() {
		for (String key : context.keys) {
			ComponentBuilderFactory cbf = context.tab_cbf.get(key);
			ComponentBuilder cb = cbf.newBuilder();
			context.tab_cb.put(key, cb);
			VPath path = context.tab_path.get(key);

			if (cb instanceof MutablePathNode) {
				MutablePathNode node = (MutablePathNode) cb;
				node.setPath(path);
			}

		}
	}

	private static void put_values_in_a2b(Map<String, String> a,
			MutableProperties b) {
		Set<Entry<String, String>> list = a.entrySet();
		for (Entry<String, String> item : list) {
			String key = item.getKey();
			String val = item.getValue();
			b.setProperty(key, val);
		}
	}

}
