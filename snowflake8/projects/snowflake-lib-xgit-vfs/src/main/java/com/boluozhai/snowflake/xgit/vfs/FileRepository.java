package com.boluozhai.snowflake.xgit.vfs;

import com.boluozhai.snow.vfs.VFileNode;
import com.boluozhai.snowflake.xgit.repository.Repository;

public interface FileRepository extends Repository, VFileNode {

	FileRepositoryContext context();

}
