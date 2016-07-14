package com.boluozhai.snowflake.xgit;

import com.boluozhai.snow.mvc.ModelContext;
import com.boluozhai.snow.mvc.model.ComponentContext;
import com.boluozhai.snowflake.xgit.repository.Repository;

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

		String workspace = "workspace"; // the working directory
		String repository = "repository";

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

	Repository repository();

}
