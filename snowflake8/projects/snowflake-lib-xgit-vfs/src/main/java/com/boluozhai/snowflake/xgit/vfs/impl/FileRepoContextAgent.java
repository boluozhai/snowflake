package com.boluozhai.snowflake.xgit.vfs.impl;

import java.net.URI;

import com.boluozhai.snow.mvc.model.Component;
import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snow.mvc.model.Element;
import com.boluozhai.snow.vfs.VFile;
import com.boluozhai.snowflake.context.SnowContext;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.vfs.FileRepository;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;

final class FileRepoContextAgent {

	private final Facade facade;

	public FileRepoContextAgent() {
		this.facade = new Facade();
	}

	public ComponentContext getFacade() {
		return this.facade;
	}

	public void setCore(SnowContext core) {
		this.facade.core = core;
	}

	private static class Facade extends Wrapper implements
			FileRepositoryContext {

		@Override
		public Element getElement(String key) {
			return this.getBean(key, Element.class);
		}

		@Override
		public Repository repository() {
			return this.getFileRepo();
		}

		private FileRepository getFileRepo() {
			return this.getBean(XGitContext.component.repository,
					FileRepository.class);
		}

		@Override
		public Component getRootComponent() {
			return this.repository();
		}

		@Override
		public VFile getFile() {
			return this.getFileRepo().getFile();
		}

	}

	private static class Wrapper implements SnowContext {

		SnowContext core;

		public String getName() {
			return core.getName();
		}

		public String getDescription() {
			return core.getDescription();
		}

		public long getBirthday() {
			return core.getBirthday();
		}

		public URI getURI() {
			return core.getURI();
		}

		public SnowContext getParent() {
			return core.getParent();
		}

		public String[] getAttributeNames() {
			return core.getAttributeNames();
		}

		public String[] getParameterNames() {
			return core.getParameterNames();
		}

		public String[] getPropertyNames() {
			return core.getPropertyNames();
		}

		public String[] getEnvironmentNames() {
			return core.getEnvironmentNames();
		}

		public Object getBean(String name) {
			return core.getBean(name);
		}

		public String getProperty(String name) {
			return core.getProperty(name);
		}

		public Object getAttribute(String name) {
			return core.getAttribute(name);
		}

		public String getParameter(String name) {
			return core.getParameter(name);
		}

		public String getEnvironment(String name) {
			return core.getEnvironment(name);
		}

		public <T> T getBean(String name, Class<T> type) {
			return core.getBean(name, type);
		}

		public String getProperty(String name, Object defaultValue) {
			return core.getProperty(name, defaultValue);
		}

		public Object getAttribute(String name, Object defaultValue) {
			return core.getAttribute(name, defaultValue);
		}

		public String getParameter(String name, Object defaultValue) {
			return core.getParameter(name, defaultValue);
		}

		public String getEnvironment(String name, Object defaultValue) {
			return core.getEnvironment(name, defaultValue);
		}

	}

}
