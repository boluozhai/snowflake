package com.boluozhai.snowflake.xgit;

import com.boluozhai.snowflake.mvc.ModelContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.workspace.Workspace;

public interface XGitContext extends ModelContext, ComponentContext {

	/*********
	 * the Property Keys
	 */

	public interface property {

		String defaultRepositoryDirectoryName = "defaultRepositoryDirectoryName";
		String hashAlgorithm = "hashAlgorithm";
		String objectsPathPattern = "objectsPathPattern";

	}

	/*********
	 * the Component Keys
	 */

	public interface component {

		// base
		String working = "working"; // the working directory
		String repository = "repository";

		// helpers
		String hash_path_mapper = "hash_path_mapper";
		String hash_algorithm = "hash_algorithm";
		String temporary_files = "temporary_files";
		String uri_meta = "meta_by_uri";
		String id_meta = "meta_by_id";
		String users = "users";
		String repositories = "repositories";

		// files
		String config = "config";
		String discription = "discription";
		String HEAD = "HEAD";
		String index = "index";

		// directories
		String branches = "branches";
		String hooks = "hooks";
		String info = "info";
		String logs = "logs";
		String objects = "objects";
		String refs = "refs";

	}

	/****
	 * getter
	 * */

	public class GET {

		public static ObjectBank objects(XGitContext context) {
			final String key = XGitContext.component.objects;
			return context.getBean(key, ObjectBank.class);
		}

		public static Workspace workspace(XGitContext context) {
			final String key = XGitContext.component.working;
			return context.getBean(key, Workspace.class);
		}

		public static Config config(XGitContext context) {
			final String key = XGitContext.component.config;
			return context.getBean(key, Config.class);
		}

		public static Repository repository(XGitContext context) {
			final String key = XGitContext.component.repository;
			return context.getBean(key, Repository.class);
		}

	}

	Repository repository();

}
