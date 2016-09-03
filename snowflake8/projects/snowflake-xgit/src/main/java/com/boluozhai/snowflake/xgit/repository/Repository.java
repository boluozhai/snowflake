package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;

public interface Repository extends XGitComponent {

	// RepositoryDriver driver();

	XGitContext context();

	URI location();

}