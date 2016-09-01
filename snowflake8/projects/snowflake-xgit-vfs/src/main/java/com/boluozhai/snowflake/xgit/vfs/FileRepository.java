package com.boluozhai.snowflake.xgit.vfs;

import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;

public interface FileRepository extends Repository, FileXGitComponent {

	FileRepositoryContext context();

}
