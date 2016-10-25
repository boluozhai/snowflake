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

		// regular.virtual
		String working = "working"; // the working directory
		String repository = "repository";

		// regular.files
		String config = "config";
		String discription = "discription";
		String HEAD = "HEAD";
		String index = "index";

		// regular.directories
		String branches = "branches";
		String hooks = "hooks";
		String info = "info";
		String logs = "logs";
		String objects = "objects";
		String refs = "refs";

		// xgit.directories
		String temporary_files = "xgit.temporary_files";
		String uri_meta = "xgit.meta_by_uri";
		String id_meta = "xgit.meta_by_id";
		String remotes = "xgit.remotes";
		// String private_refs = "xgit.private_refs";
		String mime_types = "xgit.mime_types";

		// xgit.virtual
		String client = "xgit.client";
		String hash_path_mapper = "xgit.hash_path_mapper";
		String hash_algorithm = "xgit.hash_algorithm";
		String refptrs = "xgit.refptrs";
		String hrefs = "xgit.hrefs";

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
