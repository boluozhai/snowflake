package com.boluozhai.snowflake.xgit.vfs;

import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.vfs.base.FileRepoComponent;
import com.boluozhai.snowflake.xgit.vfs.context.FileRepositoryContext;

public interface FileRepository extends Repository, FileRepoComponent {

	FileRepositoryContext context();

}
